package io.github.tomhusky.websocket.bean;

import io.github.tomhusky.websocket.enumerate.SocketResponseType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>Socket连接返回类<p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SocketResult<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 响应类型 1 请求返现结果 2 服务端主动发起
     */
    private Integer type = 2;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 地址
     */
    private String url;

    /**
     * 响应消息体
     */
    private T body;

    public static <T> SocketResult<T> build(T body, String url, SocketResponseType responseType) {
        return new SocketResult<>(body, url, responseType);
    }

    public static <T> SocketResult<T> build(T body, String url) {
        return new SocketResult<>(body, url);
    }

    public static <T> SocketResult<T> build(String url) {
        return new SocketResult<>(url);
    }

    public SocketResult<T> fail() {
        this.status = 500;
        this.errorMsg = "请求失败";
        return this;
    }

    public SocketResult<T> fail(Integer status, String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.type = 1;
        return this;
    }

    public SocketResult<T> fail(String errorMsg) {
        this.status = 500;
        this.errorMsg = errorMsg;
        this.type = 1;
        return this;
    }

    public SocketResult<T> ok() {
        this.status = 200;
        this.type = 1;
        return this;
    }

    public SocketResult() {

    }

    private SocketResult(String url) {
        this.url = url;
    }

    private SocketResult(T body, String url) {
        this.body = body;
        this.url = url;
    }

    private SocketResult(T body, String url, SocketResponseType responseType) {
        this.body = body;
        this.url = url;
        this.type = responseType.getCode();
    }
}
