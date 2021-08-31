package io.github.tomhusky.websocket.bean;

import lombok.Data;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.socket.bean
 * @ClassName: SocketRequest
 * @CreateDate: 2019/7/31 14:49
 * @Description: webSocket请求类
 */
@Data
public class SocketRequest {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求数据
     */
    private String body;

}
