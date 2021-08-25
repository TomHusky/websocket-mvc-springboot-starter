package com.tomhusky.websocket.util;

import com.tomhusky.websocket.bean.MethodBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: SpringMVC
 * @Package: com.lwj.mvc.util
 * @ClassName: DispatcherUtil
 * @Author: lwj
 * @CreateDate: 2019/02/23 11:27
 * @UpdateDate: 2019/02/23 11:27
 * @Version: 1.0
 * @Description: 分发帮助类
 */
public class DispatcherUtil {
    private DispatcherUtil() {

    }

    /**
     * 用于分发
     *
     * @param methodBean
     * @return
     */
    public static Object response(MethodBean methodBean) {
        Method method = methodBean.getMethod();
        Object object = methodBean.getObject();
        //获取方法参数
        Object[] param = getParam(methodBean.getParamValueMap());
        //执行请求方法获取执行结果
        return runMethod(method, object, param);
    }

    private static Object runMethod(Method method, Object object, Object[] param) {
        Object invoke;
        if (param != null && param.length > 0) {
            invoke = ReflectionUtils.invokeMethod(method, object, param);
        } else {
            invoke = ReflectionUtils.invokeMethod(method, object);
        }
        return invoke;
    }

    private static Object[] getParam(Map<String, Object> map) {
        if (map == null || map.size() < 1) {
            return new Object[0];
        }
        List<Object> values = new ArrayList<>(map.values());
        return values.toArray();
    }
}
