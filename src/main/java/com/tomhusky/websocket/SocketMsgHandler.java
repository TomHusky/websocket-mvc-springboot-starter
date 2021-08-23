package com.tomhusky.websocket;

import com.tomhusky.websocket.bean.SocketRequest;
import com.tomhusky.websocket.util.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author luowj
 * @className: SocketMsgHandler
 * @date 2021/8/23 14:44
 * @version：1.0
 * @description: websocket消息处理器
 */
@Slf4j
@Component
public class SocketMsgHandler extends TextWebSocketHandler {
    /**
     * 握手成功之后 回调方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        SocketSessionManager.add(session.getId(), session);
    }

    /**
     * 接收客户端消息
     *
     * @param session
     * @param message
     * @throws Exception 前端 websocket 会发送 send Beat 的事件到后端
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("来自客户端的消息:" + message);
        SocketRequest socketRequest = null;
        try {
            socketRequest = FastJsonUtils.toObject(message.getPayload(), SocketRequest.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (socketRequest == null) {
            SocketSessionManager.sendMessages(session.getId(), "请求数据有误");
            return;
        }
        //消息分发到指定控制器
        DispatcherSocketRequest.dispatcher(message.getPayload(), session);
    }


    /**
     * 任何原因导致WebSocket连接中断时，Spring会自动调用afterConnectionClosed方法
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        SocketSessionManager.removeAndClose(session.getId());
    }

    /**
     * 消息传输出错时调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        // 消息传输出错时调用
        log.error("socket session{}", session);
    }
}
