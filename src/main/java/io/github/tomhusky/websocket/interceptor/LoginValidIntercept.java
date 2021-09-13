package io.github.tomhusky.websocket.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

/**
 * @author luowj
 * @className: LoginValidIntercept
 * @date 2021/8/27 15:01
 * @version：1.0
 * @description: 登陆校验接口
 */
public abstract class LoginValidIntercept implements ValidIntercept {

    @Override
    public abstract boolean attemptAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                                  WebSocketHandler webSocketHandler);

    @Override
    public abstract void successfulAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                                  WebSocketHandler webSocketHandler);
}
