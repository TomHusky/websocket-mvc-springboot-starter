package io.github.tomhusky.websocket;

import io.github.tomhusky.websocket.exception.MethodParamsNotValidException;
import io.github.tomhusky.websocket.util.JacksonUtil;
import io.github.tomhusky.websocket.util.ValidationAnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.socket.WebSocketSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p> 请求入参 <p/>
 *
 * @author lwj
 * @since 2021/8/27 15:01
 */
public class RequestEntry {

    private final Validator validator;

    private final ConversionService conversionService;

    public RequestEntry(Validator validator, ConversionService conversionService) {
        this.validator = validator;
        this.conversionService = conversionService;
    }

    /**
     * 方法参数值填充
     *
     * @param method  方法对象
     * @param data    数据
     * @param session websocket会话
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    public Map<String, Object> fillParam(Method method, String data, WebSocketSession session) throws MethodParamsNotValidException {
        if (method == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> paramNameValueMap = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : IocContainer.METHOD_PARAM_MAP.get(method).entrySet()) {
            Class<?> paramType = entry.getValue();
            String paramName = entry.getKey();
            Object bean;
            if (paramType == WebSocketSession.class) {
                bean = session;
            } else {
                bean = JacksonUtil.parseObject(data, paramType);
                int size = paramNameValueMap.size() - 1;
                if (size < 0) {
                    size = 0;
                }
                Parameter parameter = method.getParameters()[size];
                // 参数校验
                validateIfApplicable(parameter, bean, paramName);
            }
            paramNameValueMap.put(paramName, bean);
        }
        return paramNameValueMap;
    }

    protected void validateIfApplicable(Parameter parameter, Object bean, String paramName) throws MethodParamsNotValidException {
        if (parameter == null) {
            return;
        }
        DataBinder dataBinder = new DataBinder(bean, paramName);
        dataBinder.setValidator(validator);
        dataBinder.setConversionService(conversionService);
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation ann : annotations) {
            Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
            if (validationHints != null) {
                dataBinder.validate(validationHints);
                break;
            }
        }
        if (dataBinder.getBindingResult().hasErrors()) {
            throw new MethodParamsNotValidException(dataBinder.getBindingResult());
        }
    }
}
