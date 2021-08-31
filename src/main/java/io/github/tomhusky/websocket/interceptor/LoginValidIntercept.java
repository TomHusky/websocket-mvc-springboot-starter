package io.github.tomhusky.websocket.interceptor;


import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
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
public class LoginValidIntercept implements ValidIntercept {

    /**
     * token 保存,60分钟过期
     */
    TimedCache<String, String> tokenCache = CacheUtil.newTimedCache(1000 * 60 * 60);

    @Override
    public boolean attemptAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler) {

        return true;
    }

    @Override
    public void successfulAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler) {

    }
}
