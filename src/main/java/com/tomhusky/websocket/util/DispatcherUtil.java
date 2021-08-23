package com.tomhusky.websocket.util;

import com.tomhusky.websocket.bean.MethodBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
    private DispatcherUtil(){

    }

    /**
     * 用于分发
     *
     * @param methodBean
     * @return
     */
    public static Map<String, String> response(MethodBean methodBean) throws InvocationTargetException, IllegalAccessException {
        HashMap<String, String> responseMap = new HashMap<>(1);
        Method method = methodBean.getMethod();
        Object object = methodBean.getObject();
        //获取方法参数
        Object[] param = getParam(methodBean.getMap());
        //执行请求方法获取执行结果
        Object invoke = runMethod(method, object, param);
        responseMap.put("data", FastJsonUtils.toString(invoke));
        return responseMap;
    }

    private static Object runMethod(Method method, Object object, Object[] param) throws InvocationTargetException, IllegalAccessException {
        Object invoke = null;
        if (param != null && param.length > 0) {
            invoke = method.invoke(object, param);
        } else {
            invoke = method.invoke(object);
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
