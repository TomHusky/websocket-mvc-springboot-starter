package com.tomhusky.websocket.listener;

import com.tomhusky.websocket.annotation.SocketController;
import com.tomhusky.websocket.annotation.SocketRequestMapping;
import com.tomhusky.websocket.configuration.WebSocketProperties;
import com.tomhusky.websocket.enumerate.IocContainer;
import com.tomhusky.websocket.exception.ControllerInvalidException;
import com.tomhusky.websocket.util.ClassUtil;
import com.tomhusky.websocket.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.socket.WebSocketSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.socket.listener
 * @ClassName: ApplicationStartup
 * @CreateDate: 2019/7/31 14:36
 * @Version: 1.0
 * @Description: spring容器初始化监听类
 */
@Slf4j
public class WebSocketMvcStart {

    private final WebSocketProperties properties;

    private final ApplicationContext applicationContext;

    public WebSocketMvcStart(WebSocketProperties properties, ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    private void init() {
        try {
            //2.初始化控制器Bean
            initController();
            //3.初始化映射器
            handlerMapping();
            System.out.println("========================WebSocket-【MVC】========================");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 初始化控制器
     */
    private void initController() {
        //扫包
        String baseControllerPackage = properties.getBasePackage();
        if (StringUtils.isBlank(baseControllerPackage)) {
            throw new RuntimeException("请在配置文件中添加扫包范围");
        } else {
            List<Class<?>> classList = ClassUtil.getClasses(baseControllerPackage);
            for (Class clazz : classList) {
                Annotation controller = clazz.getDeclaredAnnotation(SocketController.class);
                if (controller != null) {
                    String name = toLowerCaseFirstOne(clazz.getSimpleName());
                    //因为需要用到spring自带的依赖注入，直接获取spring中的对象
                    Object bean = applicationContext.getBean(name);
                    IocContainer.objMap.put(name, bean);
                }
            }
        }
    }

    /**
     * 处理器映射
     */
    private void handlerMapping() {
        for (Map.Entry entry : IocContainer.objMap.entrySet()) {
            Object controller = entry.getValue();
            String oneUrl = "/";

            Class<?> clazz = controller.getClass();
            SocketRequestMapping requestMapping = clazz.getDeclaredAnnotation(SocketRequestMapping.class);
            //判断类上是否加入RequestMapping注解
            if (requestMapping != null) {
                String value = requestMapping.value();
                //判断RequestMapping中的路径是否有加 "/ "
                if (!value.startsWith("/")) {
                    oneUrl = oneUrl + requestMapping.value();
                } else {
                    oneUrl = requestMapping.value();
                }
            }
            //获取控制器上所有方法
            Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(clazz);
            for (Method method : declaredMethods) {
                SocketRequestMapping mRequestMapping = method.getDeclaredAnnotation(SocketRequestMapping.class);
                if (mRequestMapping != null) {
                    if ("".equals(mRequestMapping.value().trim())) {
                        throw new ControllerInvalidException("方法映射地址不能为空");
                    }
                    String url = oneUrl + mRequestMapping.value();
                    IocContainer.methodMap.put(url, method);
                    IocContainer.urlObjMap.put(url, controller);
                    //保存方法参数
                    saveMethodParam(method);
                }
            }
        }
    }

    /**
     * 获取方法的参数名和类型,只允许方法有一个参数
     */
    private void saveMethodParam(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 1 && parameters[0].getType() != WebSocketSession.class) {
            throw new ControllerInvalidException("requestMapping修饰的方法只能有一个非WebSocketSession类型参数");
        }
        Map<String, Class> map = new HashMap<>(parameters.length);
        for (Parameter parameter : parameters) {
            map.put(parameter.getName(), parameter.getType());
        }
        IocContainer.methodParamMap.put(method, map);
    }

    /**
     * 首字母转小写
     */
    private String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

}
