package io.github.tomhusky.websocket.util;

import io.github.tomhusky.websocket.bean.MethodBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p> 请求分发帮助类 <p/>
 *
 * @author lwj
 * @since 2021/8/27 15:01
 */
public class DispatcherUtil {

    private DispatcherUtil() {

    }

    /**
     * 分发核心方法
     *
     * @param methodBean 方法参数
     * @return java.lang.Object
     */
    public static Object response(MethodBean methodBean) {
        Method method = methodBean.getMethod();
        Object object = methodBean.getObject();
        //获取方法参数
        Object[] param = getParam(methodBean.getParamValueMap());
        //执行请求方法获取执行结果
        return runMethod(method, object, param);
    }

    /**
     * 解析方法参数
     *
     * @param method 方法对象
     * @param object 方法对应的实体
     * @param param  方法参数
     * @return java.lang.Object
     */
    private static Object runMethod(Method method, Object object, Object[] param) {
        Object invoke;
        if (param != null && param.length > 0) {
            invoke = ReflectionUtils.invokeMethod(method, object, param);
        } else {
            invoke = ReflectionUtils.invokeMethod(method, object);
        }
        return invoke;
    }

    /**
     * map转数组
     *
     * @param map map对象
     * @return java.lang.Object[]
     */
    private static Object[] getParam(Map<String, Object> map) {
        if (map == null || map.size() < 1) {
            return new Object[0];
        }
        List<Object> values = new ArrayList<>(map.values());
        return values.toArray();
    }
}
