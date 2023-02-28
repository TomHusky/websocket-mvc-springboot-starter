package io.github.tomhusky.websocket.listener;

import io.github.tomhusky.websocket.SocketSendMsgManager;
import io.github.tomhusky.websocket.SocketSessionManager;
import io.github.tomhusky.websocket.bean.RedisTopicMsg;
import io.github.tomhusky.websocket.configuration.WebSocketProperties;
import io.github.tomhusky.websocket.util.ClusterHolder;
import io.github.tomhusky.websocket.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * <p>
 * redis订阅消息处理
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2023/1/29 10:26
 */
@Slf4j
public class RedisListenerHandle extends MessageListenerAdapter {

    private final RedisTemplate<String, String> redisTemplate;

    private final String subscribeTopic;

    public RedisListenerHandle(WebSocketProperties socketProperties, RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.subscribeTopic = socketProperties.getCluster().getTopic();
    }

    /**
     * 收到监听消息
     */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String rawMsg;
        String topic;
        try {
            // 反序列化数据
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);
            log.debug("Received raw message from topic:" + topic + ", raw message content：" + rawMsg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
        RedisTopicMsg topicMsg = JacksonUtil.parseObject(rawMsg, RedisTopicMsg.class);
        if (subscribeTopic.equals(topic) && topicMsg != null) {
            log.debug("Send message to all users:" + rawMsg);
            if (ClusterHolder.isCluster() && !SocketSessionManager.exit(topicMsg.getSessionId())) {
                return;
            }
            // 发送消息
            SocketSendMsgManager.sendMessages(topicMsg.getSessionId(), topicMsg.getSocketResult());
        } else {
            log.warn("No further operation with this topic!");
        }
    }
}
