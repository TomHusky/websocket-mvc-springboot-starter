package io.github.tomhusky.websocket.context;

/**
 * <p>
 * websocket上下文对象
 * 目前只有 WebSocketSession一个字段，后续可能拓展
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/9/27 17:40
 */
public final class WebSocketContext {

    private final SessionDetail sessionDetail;

    public WebSocketContext(SessionDetail session) {
        this.sessionDetail = session;
    }

    public SessionDetail getSessionDetail() {
        return sessionDetail;
    }
}
