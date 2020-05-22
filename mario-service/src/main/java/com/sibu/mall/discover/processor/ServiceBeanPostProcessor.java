package com.mall.discover.processor;

import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Description:修复dubbo2.7.1 zk 注册服务为 持久化节点问题
 * Author: wei
 * Date：2019/5/7
 */
@Component
public class ServiceBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(ServiceBean.class.isInstance(bean)){
            ((ServiceBean)bean).setDynamic(Boolean.TRUE);
        }
        return bean;
    }
}
