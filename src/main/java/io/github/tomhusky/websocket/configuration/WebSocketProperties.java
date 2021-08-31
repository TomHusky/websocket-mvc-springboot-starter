package io.github.tomhusky.websocket.configuration;

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
@ConfigurationProperties(prefix = "web-socket-mvc")
public class WebSocketProperties {

    /**
     * 扫包路径
     */
    private String basePackage;

    /**
     * token签名秘钥
     */
    private String signKey;

    /**
     * 开启身份验证
     */
    private Boolean enableValid;

    private String loginPath;
}
