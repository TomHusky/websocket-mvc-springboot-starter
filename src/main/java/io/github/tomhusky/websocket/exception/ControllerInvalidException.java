package io.github.tomhusky.websocket.exception;

/**
 * @Author: lwj
 * @Package: com.gzfyit.iot.flowerpot.exception
 * @ClassName: ControllerInvalidException
 * @CreateDate: 2019/7/31 16:44
 * @Version: 1.0
 * @Description: 初始化自定义控制器时的异常
 */
public class ControllerInvalidException extends RuntimeException {

    public ControllerInvalidException(){
        super("控制器编写有问题，请检查");
    }

    public ControllerInvalidException(String message){
        super(message);
    }
}
