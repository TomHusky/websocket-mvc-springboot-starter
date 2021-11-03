package io.github.tomhusky.websocket.context;

import org.springframework.web.socket.WebSocketSession;

/**
 * <p>
 * 默认回话对象
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/9/28 10:03
 */
public final class DefaultSessionDetail implements SessionDetail {

    private final WebSocketSession session;

    public DefaultSessionDetail(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public WebSocketSession getWebSocketSession() {
        return this.session;
    }
}
