package io.github.tomhusky.websocket.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * <p> 登陆校验拦截规范接口 <p/>
 *
 * @author lwj
 * @date 2021/8/27 15:01
 */
public interface ValidIntercept {
    /**
     * 校验方法，握手之前调用。返回ture则认为校验成功
     *
     * @param serverHttpRequest:  请求
     * @param serverHttpResponse: 响应
     * @return boolean
     */
    boolean attemptAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);

    /**
     * 握手成功之后调用
     *
     * @param serverHttpRequest:  请求
     * @param serverHttpResponse: 响应
     */
    void successfulAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);
}
