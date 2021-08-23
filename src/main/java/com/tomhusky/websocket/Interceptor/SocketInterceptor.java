package com.tomhusky.websocket.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author luowj
 * @className: SocketInterceptor
 * @date 2021/8/23 14:33
 * @version：1.0
 * @description: 在Websocket连接建立阶段（此时还是HTTP协议）拦截HTTP请求，获取到HttpSesion并保存。 webSocket握手拦截器 可用于验证登录
 */
@Slf4j
@Component
public class SocketInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * websocket 握手之前
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map                websocket 中的 attributes
     * @return true 继续执行，false 终端
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler,
                                   Map<String, Object> map) throws Exception {
        //TODO 获取token 校验


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
    }

}
