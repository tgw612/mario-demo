package com.mall.discover.mq.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * ApplicationContextHelper Utils
 *
 * @author kevin.jia
 * @since 2019/7/22 14:20
 */
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }
}