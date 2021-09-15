package io.github.tomhusky.websocket.annotation;

import java.lang.annotation.*;

/**
 * <p>用于处理Socket请求的注解<p/>
 *
 * @author lwj
 * @date 2019/7/31 14:41
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SocketRequestMapping {
    /**
     * 请求地址
     */
    String value() default "";

    /**
     * 请求类型
     */
    String method() default "";
}
