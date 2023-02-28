package io.github.tomhusky.websocket;

import io.github.tomhusky.websocket.bean.RedisTopicMsg;
import io.github.tomhusky.websocket.bean.SocketResult;
import io.github.tomhusky.websocket.util.ClusterHolder;
import io.github.tomhusky.websocket.util.JacksonUtil;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * 发送消息 工具
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2023/1/29 15:38
 */
public class SocketSendMsgManager {

    private static volatile RedisTemplate<String, String> redisTemplate;

    public SocketSendMsgManager(RedisTemplate<String, String> redisTemplate) {
        SocketSendMsgManager.redisTemplate = redisTemplate;
    }

    public SocketSendMsgManager() {

    }

    /**
     * 发送消息
     *
     * @param sessionId    sessionId
     * @param socketResult 数据
     */
    public static <T> void sendMessages(String sessionId, SocketResult<T> socketResult) {
        if (ClusterHolder.isCluster()) {
            RedisTopicMsg topicMsg = new RedisTopicMsg();
            topicMsg.setSessionId(sessionId);
            topicMsg.setSocketResult(socketResult);
            redisTemplate.convertAndSend(ClusterHolder.redisTopic(), JacksonUtil.toJSONString(topicMsg));
        } else {
            SocketSessionManager.sendMessages(sessionId, socketResult);
        }
    }
}
