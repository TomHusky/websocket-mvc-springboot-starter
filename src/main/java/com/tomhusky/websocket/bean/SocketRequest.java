package com.tomhusky.websocket.bean;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.socket.bean
 * @ClassName: SocketRequest
 * @CreateDate: 2019/7/31 14:49
 * @Description: webSocket请求类
 */
public class SocketRequest {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求数据
     */
    private Object data;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SocketRequest{" +
                "url='" + url + '\'' +
                ", data=" + data +
                '}';
    }
}
