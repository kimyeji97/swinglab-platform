package com.dailystudy.swinglab.service.framework.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
       context = applicationContext;
    }
    
    public static <T> T getBean(Class<T> beanClass) {
       return context.getBean(beanClass);
    }
    
    public static Object getBean(String beanName) {
       return context.getBean(beanName);
    }
 }