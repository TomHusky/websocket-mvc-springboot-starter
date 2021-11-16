package io.github.tomhusky.websocket.configuration;

import io.github.tomhusky.websocket.DispatcherSocketMsg;
import io.github.tomhusky.websocket.SocketMsgHandler;
import io.github.tomhusky.websocket.interceptor.SocketInterceptor;
import io.github.tomhusky.websocket.listener.WebSocketMvcStart;
import io.github.tomhusky.websocket.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.Validator;

/**
 * <p> Websocket 自动配置加载类 <p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
@AutoConfigureBefore({WebSocketServletAutoConfiguration.class, WebSocketAutoConfig.class})
public class WebSocketMvcAutoConfiguration {

    private final WebSocketProperties properties;

    public WebSocketMvcAutoConfiguration(WebSocketProperties properties) {
        log.debug("初始化websocket-mvc配置");
        this.properties = properties;
    }

    /**
     * Spring上下文工具配置
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("SpringContextHolder [{}]", holder);
        return holder;
    }


    @Bean(initMethod = "init")
    public WebSocketMvcStart startMvc(ApplicationContext applicationContext) {
        return new WebSocketMvcStart(properties, applicationContext);
    }

    @Bean
    public SocketInterceptor socketInterceptor(WebSocketProperties properties) {
        return new SocketInterceptor(properties);
    }

    /**
     * 登springboot自带的mvc校验器初始化化后加载
     */
    @Bean
    @ConditionalOnMissingBean(SocketMsgHandler.class)
    public SocketMsgHandler socketMsgHandler(@Qualifier("mvcValidator") Validator validator, @Qualifier("mvcConversionService") FormattingConversionService conversionService) {
        DispatcherSocketMsg dispatcherSocketMsg = new DispatcherSocketMsg(validator, conversionService);
        return new SocketMsgHandler(dispatcherSocketMsg);
    }
}
