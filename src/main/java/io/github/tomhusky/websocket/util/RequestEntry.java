package io.github.tomhusky.websocket.util;

import io.github.tomhusky.websocket.enumerate.IocContainer;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <p> 请求入参 <p/>
 *
 * @author lwj
 * @date 2021/8/27 15:01
 */
public class RequestEntry {

    private RequestEntry() {

    }

    /**
     * 方法参数值填充
     *
     * @param method: 方法对象
     * @param data: 数据
     * @param session: websocket回话
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public static Map<String, Object> fillParam(Method method, String data, WebSocketSession session) {
        if (method == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> paramNameValueMap = new TreeMap<>();
        for (Map.Entry<String, Class<?>> entry : IocContainer.METHOD_PARAM_MAP.get(method).entrySet()) {
            Class<?> paramType = entry.getValue();
            String paramName = entry.getKey();
            Object bean;
            if (paramType == WebSocketSession.class) {
                bean = session;
            } else {
                bean = FastJsonUtils.toObject(data, paramType);
            }
            paramNameValueMap.put(paramName, bean);
        }
        return paramNameValueMap;
    }
}
