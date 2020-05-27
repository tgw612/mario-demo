package com.mario.web.support.mvc;

import com.mario.web.support.exception.CustomAuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * AuthenticationException参数解析器 Author: qiujingwang
 */
public class AuthenticationExArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(CustomAuthenticationException.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    if (parameter.getParameterType().equals(CustomAuthenticationException.class)) {
      CustomAuthenticationException exception = (CustomAuthenticationException) webRequest
          .getAttribute("SPRING_SECURITY_LAST_EXCEPTION", RequestAttributes.SCOPE_SESSION);
      return exception;
    }
    return null;
  }
}
