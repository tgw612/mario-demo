package com.mario.web.support.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class XSSFilter implements Filter {

  public void destroy() {

  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    chain.doFilter(new XSSServletRequest((HttpServletRequest) request), response);
  }

  public void init(FilterConfig filterConfig) throws ServletException {

  }
}
