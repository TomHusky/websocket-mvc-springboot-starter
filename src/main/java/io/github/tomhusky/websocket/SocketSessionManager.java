package io.github.tomhusky.websocket;

import io.github.tomhusky.websocket.util.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> websocket会话容器 <p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
 */
@Slf4j
public class SocketSessionManager {

    private SocketSessionManager() {

    }

    /**
     * websocket 会话池
     * 如果想长期存在会话池，可以使用  Map<String, WebSocketSession>  来存储
     */
    private static final Map<String, WebSocketSession> WEB_SOCKET_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 添加 websocket 会话
     *
     * @param key     key
     * @param session 会话对象
     */
    public static synchronized void add(String key, WebSocketSession session) {
        WEB_SOCKET_SESSION_MAP.computeIfAbsent(key, k -> session);
    }

    /**
     * 移除 websocket 会话,并将该会话内容返回
     *
     * @param key: key
     * @return org.springframework.web.socket.WebSocketSession
     */
    public static WebSocketSession remove(String key) {
        WebSocketSession session = WEB_SOCKET_SESSION_MAP.get(key);
        WEB_SOCKET_SESSION_MAP.remove(key);
        return session;
    }

    /**
     * 删除 websocket,并关闭连接
     *
     * @param key: key
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("Websocket session close exception ", e);
            }
        }
    }

    /**
     * 获取 websocket 会话
     *
     * @param key: key
     * @return org.springframework.web.socket.WebSocketSession
     */
    public static WebSocketSession get(String key) {
        return WEB_SOCKET_SESSION_MAP.get(key);
    }

    /**
     * 发送消息
     *
     * @param key  自定义 key
     * @param data 数据
     */
    public static <T> void sendMessages(String key, T data) {
        String sendData = "";
        if (data != null) {
            try {
                // json 序列化
                sendData = FastJsonUtils.toString(data);
            } catch (Exception e) {
                log.error("json序列化异常，{}", e.getMessage());
                return;
            }
        }
        // 根据 key 缓存中获取 WebSocketSession
        WebSocketSession session = WEB_SOCKET_SESSION_MAP.get(key);
        try {
            if (session != null && session.isOpen()) {
                // 把json消息包装成 TextMessage 进行发送
                session.sendMessage(new TextMessage(sendData));
            }
        } catch (IOException e) {
            log.error("消息发送异常，{}", e.getMessage());
        }
    }
}
