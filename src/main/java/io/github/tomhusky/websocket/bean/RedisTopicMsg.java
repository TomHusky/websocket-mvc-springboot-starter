package io.github.tomhusky.websocket.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * redis发布和订阅的消息内容
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2023/1/29 14:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RedisTopicMsg {

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 消息内容
     */
    private SocketResult socketResult;

}
