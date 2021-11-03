package io.github.tomhusky.websocket.context.strategy;

import io.github.tomhusky.websocket.context.WebSocketContext;
import org.springframework.util.Assert;

/**
 * <p>
 * 本地线程内容中心处理器
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/9/27 17:38
 */
public final class ThreadLocalSocketContextHolderStrategy implements WebSocketContextHolderStrategy {

    private static final ThreadLocal<WebSocketContext> contextHolder = new ThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public WebSocketContext getContext() {
        WebSocketContext ctx = contextHolder.get();
        if (ctx == null) {
            ctx = this.createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }

    @Override
    public void setContext(WebSocketContext context) {
        Assert.notNull(context, "Only non-null WebSocketContext instances are permitted");
        contextHolder.set(context);
    }

    @Override
    public WebSocketContext createEmptyContext() {
        return new WebSocketContext(null);
    }

}
