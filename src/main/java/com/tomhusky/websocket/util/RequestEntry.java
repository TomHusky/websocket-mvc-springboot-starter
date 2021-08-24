package com.tomhusky.websocket.util;

import com.tomhusky.websocket.enumerate.IocContainer;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public static Map<String, Object> fillParam(Method method, Object data) {
        if (method == null) {
            return Collections.emptyMap();
        }

        HashMap<String, Object> paramNameValueMap = new HashMap<>(1);
        for (Map.Entry<String, Class> entry : IocContainer.methodParamMap.get(method).entrySet()) {
            Class paramType = entry.getValue();
            String paramName = entry.getKey();
            //将json数据转成Object对象
            if (StringUtils.isEmpty(data)) {
                data = "";
            }
            Object bean = FastJsonUtils.toObject(data.toString(), paramType);
            paramNameValueMap.put(paramName, bean);
        }
        return paramNameValueMap;
    }
}
