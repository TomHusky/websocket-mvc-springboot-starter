package com.tomhusky.websocket.util;

import com.tomhusky.websocket.enumerate.IocContainer;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @ProjectName: SpringMVC
 * @Package: com.lwj.mvc.util
 * @ClassName: RequestEntry
 * @Author: lwj
 * @CreateDate: 2019/02/26 9:19
 * @UpdateDate: 2019/02/26 9:19
 * @Version: 1.0
 * @Description: 请求入参
 */
public class RequestEntry {

    private RequestEntry() {

    }

    /**
     * 方法参数值填充
     */
    public static Map<String, Object> fillParam(Method method, String data, WebSocketSession session) {
        if (method == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> paramNameValueMap = new TreeMap<>();
        for (Map.Entry<String, Class> entry : IocContainer.methodParamMap.get(method).entrySet()) {
            Class paramType = entry.getValue();
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
