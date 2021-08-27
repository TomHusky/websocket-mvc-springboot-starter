package com.tomhusky.websocket.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.tomhusky.websocket.configuration.WebSocketProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author luowj
 * @className: TokenBuild
 * @date 2021/8/27 14:32
 * @version：1.0
 * @description: token生成器
 */
@Component
public class TokenBuild {

    private final WebSocketProperties webSocketProperties;


    public TokenBuild(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    public String getToken(String username) {
        JWT jwt = JWT.create().setPayload("username", username);
        if (StrUtil.isNotBlank(webSocketProperties.getSignKey())) {
            jwt.setKey(webSocketProperties.getSignKey().getBytes());
        }
        return jwt.sign();
    }

    public String getToken(String username, Map<String, Object> expandPayload) {
        JWT jwt = JWT.create().setPayload("username", username);
        if (CollectionUtil.isNotEmpty(expandPayload)) {
            for (Map.Entry<String, Object> entry : expandPayload.entrySet()) {
                jwt.setPayload(entry.getKey(), entry.getValue());
            }
        }
        if (StrUtil.isNotBlank(webSocketProperties.getSignKey())) {
            jwt.setKey(webSocketProperties.getSignKey().getBytes());
        }
        return jwt.sign();
    }

    public boolean verify(String rightToken) {
        // 默认验证HS265的算法
        JWT of = JWT.of(rightToken);
        if (StrUtil.isNotBlank(webSocketProperties.getSignKey())) {
            of.setKey(webSocketProperties.getSignKey().getBytes());
        }
        return of.verify();
    }
}
