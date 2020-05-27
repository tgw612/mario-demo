package com.mario.web.support.config;

import com.doubo.common.util.StringUtil;
import com.doubo.json.util.JsonUtil;
import com.doubo.security.autoconfigure.*;
import com.doubo.security.filter.authc.AuthenticationFilter;
import com.doubo.security.subject.Subject;
import com.doubo.security.util.ThreadContext;
import com.mall.common.exception.code.ErrorCodeEnum;
import com.mall.common.manage.ResponseManage;
import com.mario.web.support.filter.TokenUidSubject;
import com.mario.web.util.HttpUtil;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA. User: qiujingwang Date: 2018-04-10 Description:
 */
@Configuration
@EnableSecurityFilter
public class SecurityConfiguration implements SecurityFilterConfigurer {

  @Setter
  private Set<String> swaggerExcludesUrlSet;

  public SecurityConfiguration() {
  }

  @Override
  public SecurityFilterMapping getSecurityFilterMapping() {
    return SecurityFilterMapping.create()
        .filterBeanName("securityFilterRegistration")
        .urlPatterns("/*")
        .order(10);
  }

  @Override
  public void addSecurityFilterChainDefinition(SecurityFilterChainDefinitionRegistry configurer) {
    //排除
    configurer.addSecurityFilterChainDefinition("anon").addPathPatterns("/index/**").order(0);
    //游离状态，token 可选（不为空则验证）
    configurer.addSecurityFilterChainDefinition("optionAuthc")
        .addPathPatterns(getOptionAuthcPathPatterns()).order(1);
    //默认所有都必须验证通过才可以访问
    configurer.addSecurityFilterChainDefinition("authc").addPathPatterns("/**").order(2);
  }

  //游离状态，token 可选（不为空则验证）
  private String[] getOptionAuthcPathPatterns() {
    Set<String> optionAuthcUrls = new HashSet<>();
    optionAuthcUrls.add("/v2.0/client/article/**");
    optionAuthcUrls.add("/v2.0/client/discover/**");
    optionAuthcUrls.add("/v2.0/client/product/**");
    optionAuthcUrls.add("/v2.0/client/subject/**");
    optionAuthcUrls.add("/v2.0/client/like/queryLikePage/**");
    optionAuthcUrls.add("/discover/**");
    optionAuthcUrls.add("/users/**");
    optionAuthcUrls.add("/client/**");
    return optionAuthcUrls.toArray(new String[optionAuthcUrls.size()]);
  }

  @Override
  public Set<String> addSecurityExcludesPattern() {
    //排除
    Set<String> urls = new HashSet<>();
    urls.add("/error/*");
    urls.add("/druid/*");
    urls.add("/health_check");
    if (swaggerExcludesUrlSet != null) {
      urls.addAll(swaggerExcludesUrlSet);
    }
    return urls;
  }

  @Override
  public void addSecurityFilter(SecurityFilterRegistry configurer) {
    configurer.addSecurityFilter("authc", new AuthcFilter());
    //游离状态，token 可选（不为空则验证）
    configurer.addSecurityFilter("optionAuthc", new OptionAuthcFilter());
  }

  @Override
  public void applyGlobalPropertiesIfNecessary(Filter filter) {

  }

  @Override
  public Subject getSubject(HttpServletRequest request, HttpServletResponse response) {
    return new TokenUidSubject(request, response);
  }

  public static class AuthcFilter extends AuthenticationFilter {

    private static String sessionTimeJson;

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
        throws Exception {
      if (sessionTimeJson == null) {
        sessionTimeJson = JsonUtil.toJson(ResponseManage.fail(ErrorCodeEnum.SESSION_TIMEOUT));
      }
      //请重新登录
      HttpUtil.writeJsonDate(response, sessionTimeJson);
      return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
        Object mappedValue) {
      return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected Subject getSubject(HttpServletRequest request, HttpServletResponse response) {
      Subject subject = ThreadContext.getSubject();
      return subject;
    }

  }

  /**
   * 游离状态，token 可选（不为空则验证）
   */
  public static class OptionAuthcFilter extends AuthenticationFilter {

    private static String sessionTimeJson;

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
        throws Exception {
      if (sessionTimeJson == null) {
        sessionTimeJson = JsonUtil.toJson(ResponseManage.fail(ErrorCodeEnum.SESSION_TIMEOUT));
      }
      //请重新登录
      HttpUtil.writeJsonDate(response, sessionTimeJson);
      return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
        Object mappedValue) {
      TokenUidSubject subject = (TokenUidSubject) this
          .getSubject(this.getHttpServletRequest(request), this.getHttpServletResponse(response));

      //游离状态，token 可选（不为空则验证）
      return subject != null && (StringUtil.isBlank(subject.getToken()) || subject
          .isAuthenticated());
    }

    @Override
    protected Subject getSubject(HttpServletRequest request, HttpServletResponse response) {
      Subject subject = ThreadContext.getSubject();
      return subject;
    }

  }
}
