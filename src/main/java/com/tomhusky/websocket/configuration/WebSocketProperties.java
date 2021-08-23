package com.tomhusky.websocket.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author luowj
 * @className: WebSocketProperties
 * @date 2021/8/23 11:42
 * @version：1.0
 * @description: 配置类
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "webSockerMvc")
public class WebSocketProperties {

    private String basePackage;

}
