package io.github.tomhusky.websocket.configuration;

import io.github.tomhusky.websocket.interceptor.SocketInterceptor;
import io.github.tomhusky.websocket.SocketMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Author: lwj
 * @Package: com.example.websocket.config
 * @ClassName: WebSocketConfig
 * @CreateDate: 2019/7/30 16:09
 * @Version: 1.0
 * @Description: webSocket核心类
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketAutoConfig implements WebSocketConfigurer {

    @Autowired
    private SocketMsgHandler socketMsgHandler;

    @Autowired
    private SocketInterceptor socketInterceptor;

    private static final String WEB_SOCKET_PATH = "websocket";

    public WebSocketAutoConfig(){
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