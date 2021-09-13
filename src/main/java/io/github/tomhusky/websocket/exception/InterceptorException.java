package io.github.tomhusky.websocket.exception;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.exception
 * @ClassName: ControllerInvalidException
 * @CreateDate: 2019/7/31 16:44
 * @Version: 1.0
 * @Description: 初始化自定义控制器时的异常
 */
public class InterceptorException extends RuntimeException {

    public InterceptorException(){
        super("拦截器异常");
    }

    public InterceptorException(String message){
        super(message);
    }
}
