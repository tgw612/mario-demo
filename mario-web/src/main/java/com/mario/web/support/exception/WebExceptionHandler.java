package com.mario.web.support.exception;

import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.doubo.ahas.SentinelRpcExceptionResolver;
import com.doubo.common.exception.SystemException;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.threadlocal.SerialNo;
import com.doubo.common.util.ExceptionUtil;
import com.mall.common.manage.ResponseManage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;

/**
 * User: qiujingwang Date: 2016/3/17 Time: 16:16 异常处理
 */
//@ControllerAdvice
@Slf4j
//@Component
public class WebExceptionHandler {

  /**
   * 限流
   *
   * @param ex
   * @return
   */
  private CommonResponse handleRpcException(RpcException ex) {
    Throwable cause = ex.getCause();
    int count = 10;
    while (cause != null && cause instanceof RpcException && count-- > 0) {
      cause = cause.getCause();
    }
    if (cause != null) {
      //调用远程出错
      String remoteAddr = "";
      if (cause instanceof RemotingException) {
        RemotingException remotingExcep = (RemotingException) cause;
        remoteAddr = remotingExcep.getRemoteAddress().toString();

        String remoteExceMsg = remotingExcep.getMessage();
        //com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
        if (remoteExceMsg != null && remoteExceMsg
            .startsWith("com.alibaba.csp.sentinel.slots.block.SentinelRpcException")) {
          log.error("[{}] RPC SentinelRpcException remoteAddr:[{}]", SerialNo.getSerialNo(),
              remoteAddr);
//                    return SentinelRpcExceptionResolver.handle((SentinelRpcException) cause);
          return SentinelRpcExceptionResolver.FLOW_BLOCK_RESP;
        }
      }
      if (cause instanceof SentinelRpcException) {
        //TODO 本地的日志已经记录
        return SentinelRpcExceptionResolver.handle((SentinelRpcException) cause);
      }
    }

    //TODO
    log.error("[{}] RPC Exception Occur:[{}]", SerialNo.getSerialNo(),
        ExceptionUtil.getAsString(ex));
    return ResponseManage.rpcException(null);
  }

  /**
   * 当前系统自定义异常，应该抛出前记录日志，故这里不需要记录
   *
   * @param ex
   * @return
   */
//    @ExceptionHandler(SystemException.class)
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.OK)
  private CommonResponse handleSystemException(SystemException ex) {
    CommonResponse response = ResponseManage.fail(ex);
//        log.error("[{}] System Exception msg[{}].\nSome Exception Occur:[{}]", SerialNo.getSerialNo(), ex.getMessage(), ExceptionUtil.getAsString(ex));
    return response;
  }

  /**
   * 参数验证、绑定异常
   *
   * @param ex
   * @return
   */
//    @ExceptionHandler(BindException.class)
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.OK)
  private CommonResponse handleBindException(BindException ex) {
    CommonResponse response = ResponseManage.fail(ex);
    log.error("[{}] Validate error msg[{}].\nSome Exception Occur:[{}]", SerialNo.getSerialNo(),
        response.getErrorMsg(), ex.getFieldError());
    return response;
  }

  /**
   * 参数验证、绑定异常
   *
   * @param ex
   * @return
   */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.OK)
  private CommonResponse handleValidateException(MethodArgumentNotValidException ex) {
    CommonResponse response = ResponseManage.fail(ex.getBindingResult());
    log.error("[{}] Validate error msg[{}].\nSome Exception Occur:[{}]", SerialNo.getSerialNo(),
        response.getErrorMsg(), ex.getBindingResult().getFieldError());
    return response;
  }

  /**
   * 未登录异常
   * @param ex
   * @return
   *//*
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public CommonResponse handleAllException(UnauthenticatedException ex) {
        CommonResponse response = ResponseRender.renderErr(ErrorCodeEnum.SESSION_TIMEOUT);
//        log.info("[{}] Account UnauthenticatedException msg[{}].\nSome Exception Occur:[{}]", SerialNo.getSerialNo(), ex.getMessage(), ExceptionUtil.getAsString(ex));
        return response;
    }*/

  /**
   * 系统异常
   *
   * @param ex
   * @return
   */
//    @ExceptionHandler(Throwable.class)
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.OK)
  private CommonResponse handleAllException(
      Throwable ex/*, HttpServletRequest req, HttpServletResponse res*/) {
    if (ex instanceof ClientAbortException) {
      // 客户关掉了浏览器引起异常，简单记录
      log.error("[{}] System Error msg[{}] ClientAbortException", SerialNo.getSerialNo(),
          ex.getMessage());
    } else if (ex instanceof HttpRequestMethodNotSupportedException) {
      return ResponseManage.fail(ex.getMessage() + ",只支持[get,post]");
    } else {
      log.error("[{}] System Error msg[{}].\nSome Exception Occur:[{}]", SerialNo.getSerialNo(),
          ex.getMessage(), ExceptionUtil.getAsString(ex));
    }
    return ResponseManage.fail("系统异常");
  }

  /**
   * 异常统一处理
   *
   * @param request
   * @param response
   * @param handlerMethod
   * @param exception
   * @return
   */
  public CommonResponse forException(HttpServletRequest request, HttpServletResponse response,
      @Nullable HandlerMethod handlerMethod, Exception exception) {
    Throwable ex = exception;
    if (ex instanceof SystemException) {
      return handleSystemException((SystemException) ex);
    }

    if (ex instanceof BindException) {
      return handleBindException((BindException) ex);
    }

    if (ex instanceof MethodArgumentNotValidException) {
      return handleValidateException((MethodArgumentNotValidException) ex);
    }

//        BlockException.isBlockException(ex)

    //RCP
    ex = getCurrentTargetException(exception);
    if (ex instanceof RpcException) {
      return handleRpcException((RpcException) ex);
    }

    return handleAllException(ex);

  }

  public Throwable getCurrentTargetException(Exception ex) {
    Throwable targetException = ex;
    int count = 10;
    while (targetException != null && count-- > 0) {
      if (targetException instanceof InvocationTargetException) {
        InvocationTargetException invocationTargetException = (InvocationTargetException) targetException;
        if (invocationTargetException != null) {
          targetException = invocationTargetException.getTargetException();
          continue;
        }
      } else if (targetException instanceof UndeclaredThrowableException) {
        UndeclaredThrowableException undeclaredThrowableException = (UndeclaredThrowableException) targetException;
        if (undeclaredThrowableException != null) {
          targetException = undeclaredThrowableException.getUndeclaredThrowable();
          continue;
        }
      }
      break;
    }
    return targetException;
  }
}
