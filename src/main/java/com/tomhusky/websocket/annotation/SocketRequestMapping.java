package com.tomhusky.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lwj
 * @Package: com.fzfyit.aliyun.iot.flowerpot.socket.annotation
 * @ClassName: SocketRequestMapping
 * @CreateDate: 2019/7/31 14:00
 * @Version: 1.0
 * @Description: 用于处理Socket请求的注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketRequestMapping {
    String value() default "";

    String method() default "";
}
