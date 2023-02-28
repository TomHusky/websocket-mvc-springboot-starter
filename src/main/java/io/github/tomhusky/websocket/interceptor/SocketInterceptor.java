package io.github.tomhusky.websocket.interceptor;

import io.github.tomhusky.websocket.configuration.WebSocketProperties;
import io.github.tomhusky.websocket.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * <p> 在Websocket连接建立阶段（此时还是HTTP协议）拦截HTTP请求，获取到HttpSesion并保存。 webSocket握手拦截器 可用于验证登录 <p/>
 *
 * @author lwj
 * @since 2021/8/23 14:33
 */
@Slf4j
public final class SocketInterceptor extends HttpSessionHandshakeInterceptor {

    private final WebSocketProperties webSocketProperties;

    private LoginValidIntercept loginValidIntercept;

    public SocketInterceptor(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
        if (Boolean.TRUE.equals(webSocketProperties.getEnableValid())) {
            try {
                this.loginValidIntercept = SpringContextHolder.getBean(LoginValidIntercept.class);
            } catch (NullPointerException e) {
            }
        }
    }

    /**
     * websocket 握手之前
     *
     * @param serverHttpRequest 请求对象
     * @param serverHttpResponse 响应对象
     * @param webSocketHandler 处理器
     * @param map                websocket 中的 attributes
     * @return true 继续执行，false 终端
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler,
                                   Map<String, Object> map) {
        log.debug("准备握手!");
        if (Boolean.TRUE.equals(webSocketProperties.getEnableValid())) {
            String name = "Authorization";
            if (!StringUtils.isEmpty(webSocketProperties.getTokenName())) {
                name = webSocketProperties.getTokenName();
            }
            String token = serverHttpRequest.getHeaders().getFirst("Sec-WebSocket-Protocol");
            serverHttpRequest.getHeaders().set(name, token);
            serverHttpResponse.getHeaders().set("Sec-WebSocket-Protocol", token);
            return getLoginValidIntercept().attemptAuthentication(serverHttpRequest, serverHttpResponse);
        }
        return true;
    }


    /**
     * websocket 握手之后
     *
     * @param serverHttpRequest 请求对象
     * @param serverHttpResponse 响应对象
     * @param webSocketHandler 处理器
     * @param e 异常
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
        log.debug("握手完成!");
        if (Boolean.TRUE.equals(webSocketProperties.getEnableValid())) {
            getLoginValidIntercept().successfulAuthentication(serverHttpRequest, serverHttpResponse);
        }
    }

    private LoginValidIntercept getLoginValidIntercept() {
        if (this.loginValidIntercept == null) {
            this.loginValidIntercept = SpringContextHolder.getBean(LoginValidIntercept.class);
        }
        return this.loginValidIntercept;
    }
}
