package io.github.tomhusky.websocket.context.strategy;

import io.github.tomhusky.websocket.context.WebSocketContext;

/**
 * <p>
 * socket上下文处理接口
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/9/27 17:40
 */
public interface WebSocketContextHolderStrategy {

    void clearContext();

    WebSocketContext getContext();

    void setContext(WebSocketContext context);

    WebSocketContext createEmptyContext();
}