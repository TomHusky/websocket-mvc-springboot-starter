package io.github.tomhusky.websocket.exception;

/**
 * <p> 初始化自定义控制器时的异常 <p/>
 *
 * @author lwj
 * @since 2019/7/31 14:41
 */
public class ControllerInvalidException extends RuntimeException {

    public ControllerInvalidException() {
        super("控制器编写有问题，请检查");
    }

    public ControllerInvalidException(String message) {
        super(message);
    }
}
