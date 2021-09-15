package io.github.tomhusky.websocket.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;


/**
 * <p> 登陆校验接口 <p/>
 *
 * @author lwj
 * @date 2021/8/27 15:01
 */
public abstract class LoginValidIntercept implements ValidIntercept {

    @Override
    public abstract boolean attemptAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);

    @Override
    public abstract void successfulAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);
}
