package io.github.tomhusky.websocket.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.tomhusky.websocket.annotation.ContentSerializer;
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
    @JsonDeserialize(using = ContentSerializer.class)
    private String body;

}
