package com.tomhusky.websocket.configuration;

import com.tomhusky.websocket.listener.WebSocketMvcStart;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luowj
 * @className: WebSocketAutoConfiguration
 * @date 2021/8/23 11:40
 * @version：1.0
 * @description: websocker 自动配置加载类
 */
@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
@AutoConfigureBefore(WebSocketServletAutoConfiguration.class)
public class WebSocketAutoConfiguration {

    private final WebSocketProperties properties;

    public WebSocketAutoConfiguration(WebSocketProperties properties) {
        this.properties = properties;
    }

    @Bean(initMethod = "init")
    public WebSocketMvcStart startMvc() {
        return new WebSocketMvcStart(properties);
    }

}
