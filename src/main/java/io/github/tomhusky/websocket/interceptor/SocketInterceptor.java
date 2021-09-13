package io.github.tomhusky.websocket.interceptor;

import io.github.tomhusky.websocket.configuration.WebSocketProperties;
import io.github.tomhusky.websocket.exception.InterceptorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author luowj
 * @className: SocketInterceptor
 * @date 2021/8/23 14:33
 * @version：1.0
 * @description: 在Websocket连接建立阶段（此时还是HTTP协议）拦截HTTP请求，获取到HttpSesion并保存。 webSocket握手拦截器 可用于验证登录
 */
@Slf4j
public class SocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Resource
    private ApplicationContext applicationContext;

    private final WebSocketProperties webSocketProperties;

    private LoginValidIntercept loginValidIntercept;

    public SocketInterceptor(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
        if (Boolean.TRUE.equals(webSocketProperties.getEnableValid())) {
            try {
                this.loginValidIntercept = applicationContext.getBean(LoginValidIntercept.class);
            } catch (NullPointerException e) {
                throw new InterceptorException("ValidIntercept impl is null");
            }
        }
    }

    /**
     * websocket 握手之前
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map                websocket 中的 attributes
     * @return true 继续执行，false 终端
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler,
                                   Map<String, Object> map) {
        if (Boolean.TRUE.equals(webSocketProperties.getEnableValid())) {
            return loginValidIntercept.attemptAuthentication(serverHttpRequest, serverHttpResponse, webSocketHandler);
        }
        log.debug("准备握手!");
        return true;
    }


    /**
     * websocket 握手之后
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
                               ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler,
                               Exception e) {
        log.debug("握手完成!");
        if (Boolean.TRUE.equals(webSocketProperties.getEnableValid())) {
            loginValidIntercept.successfulAuthentication(serverHttpRequest, serverHttpResponse, webSocketHandler);
        }
    }

}
