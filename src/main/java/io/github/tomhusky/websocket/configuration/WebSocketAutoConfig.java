package io.github.tomhusky.websocket.configuration;

import io.github.tomhusky.websocket.SocketMsgHandler;
import io.github.tomhusky.websocket.interceptor.SocketInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * <p> webSocket核心类 <p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketAutoConfig implements WebSocketConfigurer {

    @Resource
    private SocketMsgHandler socketMsgHandler;

    @Resource
    private SocketInterceptor socketInterceptor;

    private static final String WEB_SOCKET_PATH = "websocket";

    public WebSocketAutoConfig() {
        log.debug("加载websocket配置...");
    }

    /**
     * @param webSocketHandlerRegistry 把webscoket信息注册进去
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                // 配置业务处理逻辑和websocket 链接端口，可以多次调用 .addHandler（） ，配置多个
                // 前端的链接地址是：ws:// + location.host （ip：port） +  WEB_SOCKET_PATH
                // 例如 ：ws://localhost:63343/websocket   后面可以带上参数
                .addHandler(socketMsgHandler, WEB_SOCKET_PATH)
                .addInterceptors(socketInterceptor)
                // 配置支持跨域
                .setAllowedOrigins("*");
    }
}