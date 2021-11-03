package io.github.tomhusky.websocket;

import io.github.tomhusky.websocket.bean.MethodBean;
import io.github.tomhusky.websocket.bean.SocketRequest;
import io.github.tomhusky.websocket.bean.SocketResult;
import io.github.tomhusky.websocket.enumerate.IocContainer;
import io.github.tomhusky.websocket.exception.MethodParamsNotValidException;
import io.github.tomhusky.websocket.util.DispatcherUtil;
import io.github.tomhusky.websocket.util.RequestEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p> 用于处理webSocket的消息，分发给相对应的处理器 <p/>
 *
 * @author lwj
 * @since 2019/7/31 14:45
 */
@Slf4j
public final class DispatcherSocketMsg {

    private final RequestEntry requestEntry;

    private ConversionService conversionService;

    private Validator validator;

    public DispatcherSocketMsg(Validator validator, ConversionService conversionService) {
        requestEntry = new RequestEntry(validator, conversionService);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    /**
     * 请求分发
     *
     * @param socketRequest socket请求
     * @param session       websocket会话对象
     */
    public void dispatcher(SocketRequest socketRequest, WebSocketSession session) {
        try {
            if (socketRequest == null || socketRequest.getUrl() == null) {
                SocketSessionManager.sendMessages(session.getId(), SocketResult.build("").fail("数据格式错误"));
                return;
            }
            //请求地址
            String requestURI = socketRequest.getUrl();
            //获取url映射的对象
            Object obj = IocContainer.URL_OBJ_MAP.get(requestURI);
            if (obj == null) {
                SocketSessionManager.sendMessages(session.getId(), SocketResult.build(socketRequest.getUrl()).fail("请求url不存在"));
            } else {
                //获取对应请求方法
                Method method = IocContainer.METHOD_MAP.get(requestURI);
                if (method == null) {
                    SocketSessionManager.sendMessages(session.getId(), SocketResult.build(socketRequest.getUrl()).fail("找不到对应的SocketRequestMapping处理器"));
                    return;
                }
                //获取请求入参值
                Map<String, Object> paramNameValueMap = requestEntry.fillParam(method, socketRequest.getBody(), session);
                MethodBean methodBean = new MethodBean(obj, method, paramNameValueMap);
                //获取响应的数据和类型
                Object response = DispatcherUtil.response(methodBean);
                //返回数据
                SocketSessionManager.sendMessages(session.getId(), SocketResult.build(response, socketRequest.getUrl()).ok());
            }
        } catch (MethodParamsNotValidException e) {
            log.error(e.getMessage(), e);
            SocketSessionManager.sendMessages(session.getId(), SocketResult.build("").fail(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
