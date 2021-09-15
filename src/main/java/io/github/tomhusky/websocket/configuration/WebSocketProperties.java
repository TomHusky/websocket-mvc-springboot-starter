package io.github.tomhusky.websocket.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p> Websocket 配置类 <p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
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
     * token字段名称
     */
    private String tokenName;

    /**
     * 开启身份验证
     */
    private Boolean enableValid;

    /**
     * 登录验证接口
     */
    private String loginPath;
}
