package com.tomhusky.websocket.bean;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ProjectName: SpringMVC
 * @Package: com.lwj.mvc.util
 * @ClassName: MethodBean
 * @Author: lwj
 * @CreateDate: 2019/02/25 22:50
 * @UpdateDate: 2019/02/25 22:50
 * @Version: 1.0
 * @Description:
 */
public class MethodBean {
    /**
     * 方法属于的对象
     */
    private Object object;
    /**
     * 方法本身
     */
    private Method method;
    /**
     * 方法的参数
     */
    private Map<String,Object> map;

    public MethodBean() {
    }

    public MethodBean(Object object, Method method, Map<String,Object> map) {
        this.object = object;
        this.method = method;
        this.map = map;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String,Object> getMap() {
        return map;
    }

    public void setMap(Map<String,Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "MethodBean{" +
                "object=" + object +
                ", method=" + method +
                ", map=" + map +
                '}';
    }
}
