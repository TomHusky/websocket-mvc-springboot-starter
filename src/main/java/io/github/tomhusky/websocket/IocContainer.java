package io.github.tomhusky.websocket;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>用于存储控制器的信息<p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
 */
public class IocContainer {
    private IocContainer() {

    }

    /**
     * 存储控制器对象  key为类名小写
     */
    protected static final ConcurrentMap<String, Object> OBJ_MAP = new ConcurrentHashMap<>();

    /**
     * 存储控制器的方法名  key为请求路径
     */
    protected static final ConcurrentMap<String, Method> METHOD_MAP = new ConcurrentHashMap<>();

    /**
     * 存储方法参数 key为方法名称  里面map的key为参数名
     */
    protected static final ConcurrentMap<Method, Map<String, Class<?>>> METHOD_PARAM_MAP = new ConcurrentHashMap<>();

    /**
     * 存路径和对象 key为请求路径
     */
    protected static final ConcurrentMap<String, Object> URL_OBJ_MAP = new ConcurrentHashMap<>();

}
