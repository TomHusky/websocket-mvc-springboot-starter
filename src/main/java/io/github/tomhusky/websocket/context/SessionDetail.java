package io.github.tomhusky.websocket.context;

import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;

/**
 * <p>
 * 回话对象
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/9/28 10:03
 */
public interface SessionDetail extends Serializable {

    WebSocketSession getWebSocketSession();
}
