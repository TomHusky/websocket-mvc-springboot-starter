package io.github.tomhusky.websocket.configuration;

import cn.hutool.core.util.StrUtil;
import io.github.tomhusky.websocket.SocketMsgHandler;
import io.github.tomhusky.websocket.interceptor.LoginValidIntercept;
import io.github.tomhusky.websocket.interceptor.SocketInterceptor;
import io.github.tomhusky.websocket.listener.LoginServlet;
import io.github.tomhusky.websocket.listener.WebSocketMvcStart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luowj
 * @className: WebSocketAutoConfiguration
 * @date 2021/8/23 11:40
 * @version：1.0
 * @description: websocker 自动配置加载类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
@AutoConfigureBefore({WebSocketServletAutoConfiguration.class, WebSocketAutoConfig.class})
public class WebSocketMvcAutoConfiguration {

    private final WebSocketProperties properties;

    public WebSocketMvcAutoConfiguration(WebSocketProperties properties) {
        log.debug("初始化websocket-mvc配置");
        this.properties = properties;
    }

    @Bean(initMethod = "init")
    public WebSocketMvcStart startMvc(ApplicationContext applicationContext) {
        return new WebSocketMvcStart(properties, applicationContext);
    }

    @Bean
    public SocketInterceptor socketInterceptor(WebSocketProperties properties) {
        return new SocketInterceptor(properties);
    }

    @Bean
    public SocketMsgHandler socketMsgHandler() {
        return new SocketMsgHandler();
    }

    @ConditionalOnBean(LoginValidIntercept.class)
    @Bean
    public ServletRegistrationBean<LoginServlet> getServletRegistrationBean() {
        ServletRegistrationBean<LoginServlet> bean = new ServletRegistrationBean<>(new LoginServlet());
        //访问路径
        if (StrUtil.isNotBlank(properties.getLoginPath())) {
            bean.addUrlMappings(properties.getLoginPath());
        } else {
            bean.addUrlMappings("/websocket/login");
        }
        return bean;
    }
}
