package com.mall.discover.filter;

import org.apache.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018/9/10
 * Description:Dubbo 服务消费方远程调用过程拦截器 for 3.x
 */
//@Service
@Slf4j
public class DubboServiceFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //打印调用前日志
//        doLogBefore(invoker, invocation);
        Result result = null;
        //调用真实远程服务
        result = invoker.invoke(invocation);
        //打印调用后日志
//        doLogAfter(invoker, result.getValue(), invocation);
        return result;
    }


    private void doLogBefore(Invoker<?> invoker, Invocation invocation) {
        log.info(BEFORE_LOG_MSG,
                getInitiationID(invocation),invoker.getInterface().getSimpleName(), invocation.getMethodName(),
                invoker.getUrl().getAddress(),  invocation.getArguments());
    }

    private void doLogAfter(Invoker<?> invoker, Object response, Invocation invocation) {
        String clazzName = invoker.getInterface().getSimpleName();   //获得类名
        String methodName = invocation.getMethodName();
        log.info(AFTER_LOG_MSG,
                getInitiationID(invocation),clazzName, methodName,  response == null?null:response.toString());
    }


    public String getInitiationID(Invocation invocation){
        Object[] args = invocation.getArguments();
        String initiationID=null;
        if(args != null && args.length >0){
            for(Object arg:args){
                if(arg instanceof String){
                    initiationID = (String)arg;
                    break;
                }
            }
        }
        return initiationID;
    }

    private static final String BEFORE_LOG_MSG = "[{}]Calling remote service[{}.{}][{}].\nSent request:{}.";
    private static final String AFTER_LOG_MSG = "[{}]Called remote service[{}.{}].\n[{}]";
}
