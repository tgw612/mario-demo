package com.mario.web.support.interceptor;

import com.doubo.common.enums.AppNameBase;
import com.doubo.trace.web.TraceClientInterceptor;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;

//import com.shop.common.enums.AppName;

/**
 * 日志记录以及流水号初始化拦截器 Created with IntelliJ IDEA. User: qiujingwang Date: 2016/3/23 Description:
 */
@Slf4j
public class SerialNoInitInterceptor extends TraceClientInterceptor {

  /**
   * @param appName
   */
  public SerialNoInitInterceptor(AppNameBase appName) {
    super(appName);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (handler instanceof HandlerMethod) {
      super.preHandle(request, response, handler);
            /*HandlerMethod handlerMethod = (HandlerMethod) handler;
            String clazzName = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            log.info("[{}] [{}.{}] start to handle request url:[{}] params:[{}]", SerialNo.getSerialNo(),
                    clazzName, methodName, request.getRequestURI(), mapToString(request.getParameterMap()));*/

      /*log.info("[{}]Start to handle request url:[{}]", SerialNo.getSerialNo(), request.getRequestURI());*/
    }
    return true;
  }

  private String mapToString(Map<String, String[]> paramMap) {
    StringBuilder stringBuilder = new StringBuilder(500);
    Set<Map.Entry<String, String[]>> entrySet = paramMap.entrySet();
    for (Map.Entry<String, String[]> entry : entrySet) {
      stringBuilder.append(entry.getKey());
      stringBuilder.append("=[");
      String[] values = entry.getValue();
      int len;
      if ((len = values.length) > 0) {
        for (int i = 0; i < len; ) {
          stringBuilder.append(values[i]);
          if ((++i) < len) {
            stringBuilder.append(",");
          }
        }
      }
      stringBuilder.append("],");
    }
    return stringBuilder.toString();
  }

}