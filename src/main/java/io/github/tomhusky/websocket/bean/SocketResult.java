package io.github.tomhusky.websocket.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>Socket连接返回类<p/>
 *
 * @author lwj
 * @date 2019/7/31 14:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SocketResult<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer status;

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
        return this;
    }

    public SocketResult<T> fail(String errorMsg) {
        this.status = 500;
        this.errorMsg = errorMsg;
        return this;
    }

    public SocketResult<T> ok() {
        this.status = 200;
        return this;
    }

    public SocketResult() {

    }

    public SocketResult(String url) {
        this.url = url;
        this.status = 200;
    }

    public SocketResult(T body, String url) {
        this.body = body;
        this.url = url;
        this.status = 200;
    }

}
