package com.tomhusky.websocket;

import com.tomhusky.websocket.bean.MethodBean;
import com.tomhusky.websocket.bean.SocketRequest;
import com.tomhusky.websocket.bean.SocketResult;
import com.tomhusky.websocket.enumerate.IocContainer;
import com.tomhusky.websocket.util.DispatcherUtil;
import com.tomhusky.websocket.util.FastJsonUtils;
import com.tomhusky.websocket.util.RequestEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.socket
 * @ClassName: DispatcherSocketRequest
 * @CreateDate: 2019/7/31 14:45
 * @Version: 1.0
 * @Description: 用于处理webSocket的消息，分发给相对应的处理器
 */
@Slf4j
public class DispatcherSocketRequest {

    private DispatcherSocketRequest() {

    }

    /**
     * 请求分发
     */
    public static void dispatcher(SocketRequest socketRequest, WebSocketSession session) {
        try {
            if (socketRequest == null || socketRequest.getUrl() == null) {
                SocketSessionManager.sendMessages(session.getId(), SocketResult.build("数据格式错误", socketRequest.getUrl()));
                return;
            }
            //请求地址
            String requestURI = socketRequest.getUrl();
            //获取url映射的对象
            Object obj = IocContainer.urlObjMap.get(requestURI);
            if (obj == null) {
                SocketSessionManager.sendMessages(session.getId(), SocketResult.build("请求url不存在", socketRequest.getUrl()));
            } else {
                //获取对应请求方法
                Method method = IocContainer.methodMap.get(requestURI);
                if (method == null) {
                    SocketSessionManager.sendMessages(session.getId(), SocketResult.build("找不到对应的SocketRequestMapping处理器", socketRequest.getUrl()));
                    return;
                }
                //获取请求入参值
                Map<String, Object> paramNameValueMap = RequestEntry.fillParam(method, socketRequest.getBody());
                MethodBean methodBean = new MethodBean(obj, method, paramNameValueMap);
                //获取响应的数据和类型
                Object response = DispatcherUtil.response(methodBean);
                //返回数据
                SocketSessionManager.sendMessages(session.getId(), SocketResult.build(response, socketRequest.getUrl()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
