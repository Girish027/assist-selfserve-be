package com.tfsc.ilabs.selfservice.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by ravi.b on 11-06-2019.
 */
@Service
public class BeanUtil implements ApplicationContextAware {
    private ApplicationContext context;

    public <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
}