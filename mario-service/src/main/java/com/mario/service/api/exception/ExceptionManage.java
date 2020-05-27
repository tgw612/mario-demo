package com.mario.service.api.exception;

import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.doubo.ahas.SentinelRpcExceptionResolver;
import com.doubo.common.model.response.CommonResponse;
import com.mall.common.exception.ValidationException;
import com.mall.common.exception.code.ErrorCodeEnum;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.persistence.exception.DataBaseException;
import org.apache.dubbo.rpc.RpcException;

/**
 * 异常处理类<br/> 1.NullPointerException 空指针异常；<br/> 2.ValidationException 校验异常；<br/> 3.RpcException
 * Dubbo超时异常；<br/> 4.Exception 其他异常。<br/>
 */
public class ExceptionManage {

  public static CommonResponse exceptionResult(Throwable throwable) {
    if (throwable instanceof IllegalArgumentException) {
      return ResponseManage.failIllegalArgument(null);

    } else if (throwable instanceof ValidationException) {
      return ResponseManage.failValidator(((ValidationException) throwable).getErrReason());

    } else if (throwable instanceof DataBaseException) {
      return ResponseManage.failDbOperation("数据库异常");

    } else if (throwable instanceof RpcException) {
      return handleRpcException((RpcException) throwable);

    } else {
      //调用者必须要记录日志
      return ResponseManage
          .fail(ErrorCodeEnum.BEAN_BIZ_ERROR.getErrorCode(), throwable.getMessage());
    }
  }

  /**
   * 限流
   *
   * @param ex
   * @return
   */
  private static CommonResponse handleRpcException(RpcException ex) {
    Throwable cause = ex.getCause();
    if (cause != null) {
      if (cause instanceof SentinelRpcException) {
        return SentinelRpcExceptionResolver.handle((SentinelRpcException) cause);
      }
    }

    //TODO
    return ResponseManage.rpcException(null);
  }
}
