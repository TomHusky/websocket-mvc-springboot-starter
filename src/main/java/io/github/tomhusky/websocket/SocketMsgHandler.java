package io.github.tomhusky.websocket;

import io.github.tomhusky.websocket.bean.SocketRequest;
import io.github.tomhusky.websocket.bean.SocketResult;
import io.github.tomhusky.websocket.context.DefaultSessionDetail;
import io.github.tomhusky.websocket.context.WebSocketContext;
import io.github.tomhusky.websocket.context.WebSocketContextHolder;
import io.github.tomhusky.websocket.util.JacksonUtil;
import io.github.tomhusky.websocket.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * <p> websocket消息处理器 <p/>
 *
 * @author lwj
 * @since 2021/8/27 15:01
 */
@Slf4j
public final class SocketMsgHandler extends TextWebSocketHandler {

    private final DispatcherSocketMsg dispatcherSocketMsg;

    private CustomerWebSocketHandler customerWebSocketHandler;


    public SocketMsgHandler(DispatcherSocketMsg dispatcherSocketMsg) {
        this.dispatcherSocketMsg = dispatcherSocketMsg;
        try {
            this.customerWebSocketHandler = SpringContextHolder.getBean(CustomerWebSocketHandler.class);
        } catch (NullPointerException e) {
            log.debug("SocketMsgHandler impl is null");
        }
    }

    /**
     * 握手成功之后 回调方法
     *
     * @param session 会话对象
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        SocketSessionManager.add(session.getId(), session);

        if (customerWebSocketHandler != null) {
            customerWebSocketHandler.afterConnectionEstablished(session);
        }
    }

    /**
     * 接收客户端消息
     *
     * @param session 会话对象
     * @param message 消息内容
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 添加到上下文对象
        WebSocketContextHolder.setContext(new WebSocketContext(new DefaultSessionDetail(session)));
        if (customerWebSocketHandler != null) {
            customerWebSocketHandler.handleMessage(session, message);
        }
        log.debug("来自客户端的消息:" + message);
        SocketRequest socketRequest;
        try {
            socketRequest = JacksonUtil.parseObject(message.getPayload(), SocketRequest.class);
            if (socketRequest == null) {
                log.error("请求数据格式有误");
                SocketSessionManager.sendMessages(session.getId(), SocketResult.build("").fail("请求数据格式有误"));
                return;
            }
            //消息分发到指定控制器
            dispatcherSocketMsg.dispatcher(socketRequest, session.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 上下文对象移除回话
            WebSocketContextHolder.clearContext();
        }
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
