package io.github.tomhusky.websocket.context;

import io.github.tomhusky.websocket.context.strategy.ThreadLocalSocketContextHolderStrategy;
import io.github.tomhusky.websocket.context.strategy.WebSocketContextHolderStrategy;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;

/**
 * <p>
 * websocket 上下文
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/9/27 17:38
 */
public final class WebSocketContextHolder {

    public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
    public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
    public static final String MODE_GLOBAL = "MODE_GLOBAL";
    public static final String SYSTEM_PROPERTY = "spring.security.strategy";
    private static String strategyName = System.getProperty("spring.security.strategy");

    private static WebSocketContextHolderStrategy strategy;

    private static int initializeCount = 0;

    public WebSocketContextHolder() {

    }

    public static void clearContext() {
        strategy.clearContext();
    }

    public static WebSocketContext getContext() {
        return strategy.getContext();
    }

    public static int getInitializeCount() {
        return initializeCount;
    }

    private static void initialize() {
        // 默认使用threadLocal
        if (!StringUtils.hasText(strategyName)) {
            strategyName = "MODE_THREADLOCAL";
        }
        if (strategyName.equals("MODE_THREADLOCAL")) {
            strategy = new ThreadLocalSocketContextHolderStrategy();
        } else if (strategyName.equals("MODE_INHERITABLETHREADLOCAL")) {

        } else if (strategyName.equals("MODE_GLOBAL")) {

        } else {
            try {
                Class<?> clazz = Class.forName(strategyName);
                Constructor<?> customStrategy = clazz.getConstructor();
                strategy = (WebSocketContextHolderStrategy) customStrategy.newInstance();
            } catch (Exception var2) {
                ReflectionUtils.handleReflectionException(var2);
            }
        }
        ++initializeCount;
    }

    public static void setContext(WebSocketContext context) {
        strategy.setContext(context);
    }

    public static void setStrategyName(String strategyName) {
        WebSocketContextHolder.strategyName = strategyName;
        initialize();
    }

    public static WebSocketContextHolderStrategy getContextHolderStrategy() {
        return strategy;
    }

    public static WebSocketContext createEmptyContext() {
        return strategy.createEmptyContext();
    }

    public String toString() {
        return "WebSocketContextHolder[strategy='" + strategyName + "'; initializeCount=" + initializeCount + "]";
    }

    static {
        initialize();
    }
}
