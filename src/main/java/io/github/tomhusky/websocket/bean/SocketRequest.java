package io.github.tomhusky.websocket.bean;

import lombok.Data;

/**
 * <p>webSocket请求参数类<p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
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
