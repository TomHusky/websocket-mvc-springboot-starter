package io.github.tomhusky.websocket.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;


/**
 * <p> 登陆校验接口 <p/>
 *
 * @author lwj
 * @since 2021/8/27 15:01
 */
public abstract class LoginValidIntercept implements ValidIntercept {
    /**
     * 校验处理方法，在握手之前被调用
     *
     * @param serverHttpRequest http请求
     * @param serverHttpResponse http响应
     * @return boolean 返回true 校验成功允许客户端连接，返回false则不允许客户端连接
     */
    @Override
    public abstract boolean attemptAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);

    /**
     * 校验通过之后会调用该方法
     *
     * @param serverHttpRequest http请求
     * @param serverHttpResponse http响应
     */
    @Override
    public abstract void successfulAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);
}
