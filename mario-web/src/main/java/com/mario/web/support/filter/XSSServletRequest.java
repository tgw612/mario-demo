package com.mario.web.support.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * xss过滤
 */
public class XSSServletRequest extends HttpServletRequestWrapper {

  HttpServletRequest orgRequest = null;

  public XSSServletRequest(HttpServletRequest request) {
    super(request);
    orgRequest = request;
  }

  /**
   * 获取最原始的request的静态方法
   *
   * @return
   */
  public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
    if (req instanceof XSSServletRequest) {
      return ((XSSServletRequest) req).getOrgRequest();
    }
    return req;
  }

  /**
   * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/> 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
   * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
   */
  public String getParameter(String s) {
    String parameter = super.getParameter(s);
    if (parameter != null) {
      parameter = xss(parameter);
    }
    return parameter;
  }

  /**
   * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/> 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/> getHeaderNames
   * 也可能需要覆盖
   */
  @Override
  public String getHeader(String s) {
    String value = super.getHeader(s);
    if (value != null) {
      value = xss(value);
    }
    return value;
  }

  public String[] getParameterValues(String s) {
    return xss(super.getParameterValues(s));
  }

  public Map getParameterMap() {
    Map<String, String[]> parameterMap = super.getParameterMap();
    Map<String, String[]> result = new HashMap<String, String[]>();
    for (Entry<String, String[]> entry : parameterMap.entrySet()) {
      result.put(entry.getKey(), xss(entry.getValue()));
    }
    return Collections.unmodifiableMap(result);
  }

  private String[] xss(String[] parameters) {
    if (parameters == null) {
      return null;
    }
    int length = parameters.length;
    String[] result = new String[length];
    for (int i = 0; i < length; i++) {
      result[i] = xss(parameters[i]);
    }
    return result;
  }

  /**
   * 将容易引起xss漏洞的特殊字符直接转义
   *
   * @param parameter
   * @return
   */
  private String xss(String parameter) {
    if (parameter == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    char[] chars = parameter.toCharArray();
    for (int i = 0, length = chars.length; i < length; i++) {
      char c = parameter.charAt(i);
      switch (c) {
        case '&':
          sb.append("&amp;");
          break;
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '"':
          sb.append("&quot;");
          break;
        case '\'':
          sb.append("&#39;");
          break;
        default:
          sb.append(c);
          break;
      }
    }
    return sb.toString();
  }

  /**
   * 获取最原始的request
   *
   * @return
   */
  public HttpServletRequest getOrgRequest() {
    return orgRequest;
  }
}
