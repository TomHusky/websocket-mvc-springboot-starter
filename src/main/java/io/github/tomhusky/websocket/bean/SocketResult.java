package io.github.tomhusky.websocket.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class SocketResult<T> implements Serializable {

    private Integer status;

    private String errorMsg;

    private String url;

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
