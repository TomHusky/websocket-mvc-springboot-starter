package io.github.tomhusky.websocket.enumerate;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.socket.enumerate
 * @ClassName: IocContainer
 * @CreateDate: 2019/7/31 14:41
 * @Description: 用于存储控制器的信息
 */

public class IocContainer {
    private IocContainer() {

    }

    /**
     * 存储控制器对象  key为类名小写
     */
    public static final ConcurrentMap<String, Object> objMap = new ConcurrentHashMap<>();

    /**
     * 存储控制器的方法名  key为请求路径
     */
    public static final ConcurrentMap<String, Method> methodMap = new ConcurrentHashMap<>();

    /**
     * 存储方法参数 key为方法名称  里面map的key为参数名
     */
    public static final ConcurrentMap<Method, Map<String, Class>> methodParamMap = new ConcurrentHashMap<>();

    /**
     * 存路径和对象 key为请求路径
     */
    public static final ConcurrentMap<String, Object> urlObjMap = new ConcurrentHashMap<>();

}
