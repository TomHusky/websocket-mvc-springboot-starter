package io.github.tomhusky.websocket.exception;

import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * <p>
 * 参数校验失败异常
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/11/3 16:32
 */
public class MethodParamsNotValidException extends Exception {

    private final BindingResult bindingResult;

    public MethodParamsNotValidException(BindingResult bindingResult) {
        Assert.notNull(bindingResult, "BindingResult must not be null");
        this.bindingResult = bindingResult;
    }


    public final BindingResult getBindingResult() {
        return this.bindingResult;
    }

    public String getMessage() {
        List<ObjectError> allErrors = this.bindingResult.getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allErrors.size(); i++) {
            sb.append(allErrors.get(i).getDefaultMessage());
            if (i != allErrors.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
