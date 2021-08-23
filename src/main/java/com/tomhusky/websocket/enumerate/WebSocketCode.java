package com.tomhusky.websocket.enumerate;

import com.tomhusky.websocket.bean.SocketResult;
import com.tomhusky.websocket.util.FastJsonUtils;

/**
 * @Author: lwj
 * @Package: com.fzfyit.aliyun.iot.flowerpot.enumerate
 * @ClassName: WebSocketCode
 * @CreateDate: 2019/7/31 11:43
 * @Version: 1.0
 * @Description: 用于webSocket通信连接响应
 */
public class WebSocketCode {
    private WebSocketCode() {

    }

    public static String connectSuccess() {
        SocketResult<Object> result = new SocketResult<>().setCode(8888).setMessage("连接成功");
        return FastJsonUtils.toString(result);
    }

    public static SocketResult connectMvcSuccess() {
        return new SocketResult().setMessage("初始化WebSocketMVC成功").setCode(9999);
    }

    public static SocketResult sendMsgSuccess() {
        return new SocketResult().setMessage("消息发送成功").setCode(10000);
    }

    public static SocketResult sendMsgFail(String msg) {
        return new SocketResult().setMessage(msg).setCode(100001);
    }
}
