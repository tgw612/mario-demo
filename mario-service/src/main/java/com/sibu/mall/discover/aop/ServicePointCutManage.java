package com.mall.discover.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 服务实现
 */
@Aspect
public class ServicePointCutManage {
    /**
     * 记录日志切点
     * 匹配impl包及任何子包下的后缀为Impl类型中的所有方法;
     */
    @SuppressWarnings("unused")
    @Pointcut("execution(* com.mall.discover.service.impl..*Impl.*(..))")
    public void logService(){

    }


    @SuppressWarnings("unused")
    @Pointcut("execution(* com.mall.discover.service.impl..*.*(..))")
    public void service(){

    }
}
