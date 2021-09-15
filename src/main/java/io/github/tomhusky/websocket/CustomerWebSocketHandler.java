package io.github.tomhusky.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * <p> 自定义消息处理 <p/>
 *
 * @author lwj
 * @date 2021/8/27 15:01
 */
public interface CustomerWebSocketHandler {

    /**
     * 且 WebSocket 连接已打开并可供使用后调用。
     *
     * @param webSocketSession: 回话对象
     */
    void afterConnectionEstablished(WebSocketSession webSocketSession);

    /**
     * 消息到达时调用
     *
     * @param webSocketSession: 回话对象
     * @param message:          消息内容
     */
    void handleMessage(WebSocketSession webSocketSession, TextMessage message);

    /**
     * 在 WebSocket 连接被任一方关闭后或发生传输错误后调用。
     *
     * @param webSocketSession: 回话对象
     * @param status:           状态码
     */
    void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status);
}