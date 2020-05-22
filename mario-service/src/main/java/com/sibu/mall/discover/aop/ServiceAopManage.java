package com.mall.discover.aop;

import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.util.CollectionUtil;
import com.doubo.common.util.ExceptionUtil;
import com.mall.common.exception.code.ErrorCodeEnum;
import com.mall.discover.common.validator.impl.ValidatorImpl;
import com.mall.discover.exception.ExceptionManage;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务实现切面处理
 * Modify by qiujingwang
 */
@Component
@Aspect
@Order(1)
@Slf4j
public class ServiceAopManage {

    @Resource
    private ValidatorImpl validator;

    @SuppressWarnings("unused")
    @Around("com.mall.discover.aop.ServicePointCutManage.logService()")
    public Object methodHandle(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] objects = proceedingJoinPoint.getArgs();
        String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String initiationID = null;
        Object request = null;
        CommonResponse response = null;
        try {

            for (int i = 0, length = objects.length; i < length; i++) {
                if (CollectionUtil.isEmpty(objects[i])) {
                    throw new IllegalArgumentException("Request must be not null");
                } else {
                    validator.validate(objects[i]);
                }
            }

            /*if(objects.length > 0){
                request= objects[0];

                if(request != null){
                    if(List.class.isInstance(request)){
                        Object el = ((List) request).get(0);
                        initiationID = el != null ? ((CommonRequest) el).getInitiationID() : null;
                    }else{
                        initiationID = ((CommonRequest) request).getInitiationID();
                    }
                }
                if(initiationID != null){
                    MDC.put(AbstractTraceFilter.INITIATION_ID_NAME, initiationID);
                    SerialNo.setSerialNo(initiationID);
                }else{
                    throw new ValidationException("请求流水号 initiationID 不能为空");
                }
            }*/

            //输入日志
            log.info("[{}][{}]Start to handle request:[{}]", className, methodName, request);
            response = (CommonResponse)proceedingJoinPoint.proceed();
        }catch (Throwable e) {
            //统一的异常管理 解析异常 返回特定的Response.
            response = ExceptionManage.exceptionResult(e);
            if(response.getErrorCode() == ErrorCodeEnum.ERR_INVALID_PARAM.getErrorCode() ||
                    response.getErrorCode() == ErrorCodeEnum.VALIDATOR_EXCEPTION.getErrorCode()){
                log.error("[{}][{}][{}] handling exception message is : [{}].", initiationID, className, methodName, e.getMessage());
            }else{
                log.error("[{}][{}][{}] handling exception message is : [{}] \n cause : [{}].", initiationID, className, methodName, e.getMessage(), ExceptionUtil.getAsString(e));
            }
        }finally {
            /*if(initiationID != null){
                SerialNo.clear();
                MDC.remove(AbstractTraceFilter.INITIATION_ID_NAME);
            }*/
            //成功
            if(response.getErrorCode() == ErrorCodeEnum.SUCCEED_CODE.getErrorCode()){
                log.info("[{}][{}][{}]Finish handling request, the response:[{}]", initiationID, className, methodName, response != null ? response.toString() : null);
            }
            return response;
        }
    }
}
