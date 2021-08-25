package com.tomhusky.websocket.bean;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

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
@Data
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
    private Map<String,Object> paramValueMap;

    public MethodBean() {
    }

    public MethodBean(Object object, Method method, Map<String,Object> paramValueMap) {
        this.object = object;
        this.method = method;
        this.paramValueMap = paramValueMap;
    }

}
