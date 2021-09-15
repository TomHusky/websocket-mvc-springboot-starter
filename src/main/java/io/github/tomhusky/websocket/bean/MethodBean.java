package io.github.tomhusky.websocket.bean;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>方法的bean对象<p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
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
