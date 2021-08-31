package io.github.tomhusky.websocket.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lwj
 * @Package: com.fzfyit.aliyun.iot.flowerpot.socket.annotation
 * @ClassName: SocketController
 * @CreateDate: 2019/7/31 13:56
 * @Description: 用于webSocket的控制器
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface SocketController {

}