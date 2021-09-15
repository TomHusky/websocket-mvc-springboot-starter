package io.github.tomhusky.websocket;

import io.github.tomhusky.websocket.bean.SocketRequest;
import io.github.tomhusky.websocket.bean.SocketResult;
import io.github.tomhusky.websocket.util.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;

/**
 * <p> websocket消息处理器 <p/>
 *
 * @author lwj
 * @date 2021/8/27 15:01
 */
@Slf4j
public final class SocketMsgHandler extends TextWebSocketHandler {

    @Resource
    private ApplicationContext applicationContext;

    private CustomerWebSocketHandler customerWebSocketHandler;

    public SocketMsgHandler() {
        try {
            this.customerWebSocketHandler = applicationContext.getBean(CustomerWebSocketHandler.class);
        } catch (NullPointerException e) {
            log.debug("SocketMsgHandler impl is null");
        }
    }

    /**
     * 握手成功之后 回调方法
     *
     * @param session: 回话对象
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if (customerWebSocketHandler != null) {
            customerWebSocketHandler.afterConnectionEstablished(session);
        }
        SocketSessionManager.add(session.getId(), session);
    }

    /**
     * 接收客户端消息
     *
     * @param session 回话对象
     * @param message 消息内容
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if (customerWebSocketHandler != null) {
            customerWebSocketHandler.handleMessage(session, message);
        }
        log.debug("来自客户端的消息:" + message);
        SocketRequest socketRequest = null;
        try {
            socketRequest = FastJsonUtils.toObject(message.getPayload(), SocketRequest.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (socketRequest == null) {
            SocketSessionManager.sendMessages(session.getId(), SocketResult.build("").fail("请求数据格式有误"));
            return;
        }
        //消息分发到指定控制器
        DispatcherSocketMsg.dispatcher(socketRequest, session);
    }


    /**
     * 任何原因导致WebSocket连接中断时，Spring会自动调用afterConnectionClosed方法
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        if (customerWebSocketHandler != null) {
            customerWebSocketHandler.afterConnectionClosed(session, status);
        }
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
