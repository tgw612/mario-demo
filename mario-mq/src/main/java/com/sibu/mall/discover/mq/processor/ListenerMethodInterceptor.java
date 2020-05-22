package com.mall.discover.mq.processor;

import com.alibaba.fastjson.JSONObject;
import com.doubo.ali.mq.model.MessageContext;
import com.doubo.ali.mq.model.MqBaseMessageBody;
import com.doubo.common.threadlocal.SerialNo;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author zhangzehao
 * @date 2019/8/13 19:43
 */
@Slf4j
public class ListenerMethodInterceptor implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        // 设置回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("deserialize".equalsIgnoreCase(method.getName())) {
            MessageContext ctx = null;
            for (Object arg : args) {
                if (arg instanceof MessageContext) {
                    ctx = (MessageContext) arg;
                    String traceId = ctx.getUserProperties("traceId");
                    log.info("ListenerMethodInterceptor.traceId = " + traceId);
                    if (StringUtils.isNotBlank(traceId)) {
                        SerialNo.setSerialNo(traceId);
                    }
                }
            }

            //处理业务逻辑
            Object result = proxy.invokeSuper(object, args);

            if (ctx != null && result != null && result instanceof MqBaseMessageBody) {
                MqBaseMessageBody body = (MqBaseMessageBody) result;
                log.info("收到MQ消息, msgId = {}, businessId = {}, content = {}", ctx.getMsgID(), body.getBusinessId(), JSONObject.toJSONString(body.getContent()));
            }
            return result;

        } else {
            //处理业务逻辑
            return proxy.invokeSuper(object, args);
        }
    }

}
