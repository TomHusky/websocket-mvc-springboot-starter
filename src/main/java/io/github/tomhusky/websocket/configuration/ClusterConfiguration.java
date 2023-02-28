package io.github.tomhusky.websocket.configuration;

import io.github.tomhusky.websocket.SocketSendMsgManager;
import io.github.tomhusky.websocket.listener.RedisListenerHandle;
import io.github.tomhusky.websocket.util.ClusterHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * <p>
 * 集群配置
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2023/2/28 11:17
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
@AutoConfigureAfter({AutoConfigureBefore.class})
@ConditionalOnProperty(prefix = "web-socket-mvc.cluster", name = "enable", havingValue = "true")
public class ClusterConfiguration {

    private final WebSocketProperties properties;

    public ClusterConfiguration(WebSocketProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RedisListenerHandle redisListenerHandle(RedisTemplate<String, String> redisTemplate) {
        return new RedisListenerHandle(properties, redisTemplate);
    }

    @Bean(name = "redisTemplate")
    public <M, T> RedisTemplate<M, T> masterObjectRedisTemplate(
            @Value("${spring.redis.host}") String hostName,
            @Value("${spring.redis.password:}") String password,
            @Value("${spring.redis.port}") int port,
            @Value("${spring.redis.database}") int database,
            @Value("${spring.redis.timeout:PT30S}") Duration timeout) {

        //redis连接配置
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //设置连接的ip
        redisStandaloneConfiguration.setHostName(hostName);
        if (!StringUtils.isEmpty(password)) {
            redisStandaloneConfiguration.setPassword(password);
        }
        if (database != 0) {
            redisStandaloneConfiguration.setDatabase(database);
        }
        //端口号
        redisStandaloneConfiguration.setPort(port);
        //JedisConnectionFactory配置jedisPoolConfig
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        //客户端超时时间单位是毫秒
        jedisClientConfiguration.connectTimeout(timeout);
        //工厂对象
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
        RedisTemplate<M, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        return redisTemplate;
    }

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        String topic = properties.getCluster().getTopic();
        // 监听topic
        container.addMessageListener(listenerAdapter, new PatternTopic(topic));
        log.info("Subscribed Redis channel: {}", topic);
        return container;
    }

    /**
     * 集群管理工具
     */
    @Bean
    public ClusterHolder clusterHolder() {
        return new ClusterHolder(properties);
    }

    @Bean
    public SocketSendMsgManager socketSendMsgManager(RedisTemplate<String, String> redisTemplate) {
        return new SocketSendMsgManager(redisTemplate);
    }
}
