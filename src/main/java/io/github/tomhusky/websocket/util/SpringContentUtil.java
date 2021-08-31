package io.github.tomhusky.websocket.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 普通类获取Bean
 *
 * @author gzfyit
 */
@Component
public class SpringContentUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private static void setContext(ApplicationContext context){
        applicationContext = context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        if (SpringContentUtil.applicationContext == null) {
            SpringContentUtil.setContext(applicationContext);
        }
    }

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String string) {
        return getApplicationContext().getBean(string);
    }

    public static <T> T getBean(Class<T> tClass) {
        return getApplicationContext().getBean(tClass);
    }

    public static <T> T getBean(String string, Class<T> tClass) {
        return getApplicationContext().getBean(string, tClass);
    }

}
