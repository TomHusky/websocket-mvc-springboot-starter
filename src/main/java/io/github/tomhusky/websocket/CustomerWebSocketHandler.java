package io.github.tomhusky.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author luowj
 * @className: CustomerWebSocketHandler
 * @date 2021/8/25 9:10
 * @version：1.0
 * @description: 自定义消息处理
 */
public interface CustomerWebSocketHandler {

    /**
     * 且 WebSocket 连接已打开并可供使用后调用。
     *
     * @param webSocketSession: 回话对象
     * @return void
     */
    void afterConnectionEstablished(WebSocketSession webSocketSession);

    /**
     * 消息到达时调用
     *
     * @param webSocketSession: 回话对象
     * @param message:          消息内容
     * @return void
     */
    void handleMessage(WebSocketSession webSocketSession, TextMessage message);

    /**
     * 在 WebSocket 连接被任一方关闭后或发生传输错误后调用。
     *
     * @param webSocketSession: 回话对象
     * @param status:           状态码
     * @return void
     */
    void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status);
}