package io.github.tomhusky.websocket.exception;

/**
 * <p> 初始化自定义控制器时的异常 <p/>
 *
 * @author lwj
 * @date 2019/7/31 14:41
 */
public class InterceptorException extends RuntimeException {

    public InterceptorException() {
        super("拦截器异常");
    }

    public InterceptorException(String message) {
        super(message);
    }
}
