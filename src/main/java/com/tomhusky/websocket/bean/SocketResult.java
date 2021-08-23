package com.tomhusky.websocket.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.socket.util
 * @ClassName: SocketResult
 * @CreateDate: 2019/8/7 15:02
 * @Description: Socket连接返回类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SocketResult<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    private T t;
}
