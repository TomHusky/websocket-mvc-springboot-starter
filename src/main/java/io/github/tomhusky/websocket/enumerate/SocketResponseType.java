package io.github.tomhusky.websocket.enumerate;

/**
 * <p> 服务端响应给客户端消息类型 <p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
 */
public enum SocketResponseType {

    /**
     * 主动返回
     */
    INITIATIVE(2),
    /**
     * 被动响应
     */
    PASSIVE(1);

    private final Integer code;

    SocketResponseType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
