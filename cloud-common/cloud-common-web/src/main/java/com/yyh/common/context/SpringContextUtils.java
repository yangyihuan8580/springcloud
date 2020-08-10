package com.yyh.common.context;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public SpringContextUtils() {

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public static Object getBean(String beanName) {
        Object obj = null;
        if(applicationContext != null) {
            obj = applicationContext.getBean(beanName);
        }

        return obj;
    }

    public static Object getBean(String beanName, Object... args) {
        Object obj = null;
        if(applicationContext != null) {
            obj = applicationContext.getBean(beanName, args);
        }

        return obj;
    }

    public static <T> T getBean(Class<T> beanClass) {
        Object obj = null;
        if(applicationContext != null) {
            obj = applicationContext.getBean(beanClass);
        }

        return (T) obj;
    }

    public static <T> T getBean(String beanName, Class<T> clsType) {
        Object obj = null;
        if(applicationContext != null) {
            obj = applicationContext.getBean(beanName, clsType);
        }

        return (T) obj;
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        Object obj = null;
        if(applicationContext != null) {
            obj = applicationContext.getBean(requiredType, args);
        }

        return (T) obj;
    }
}