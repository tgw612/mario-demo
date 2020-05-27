package com.mario.web.util;

import com.doubo.security.subject.Subject;
import com.doubo.security.util.ThreadContext;

/**
 * 用户Token工具类
 *
 * @author: xiaoqiang.gan@doubozhibo.com Date: 2019/2/18 20:33
 */
public class AccessTokenUtil {

  public static Integer getCurrentUserId() {
    Subject subject = ThreadContext.getSubject();
    if (subject == null || !subject.isAuthenticated()) {
      return null;
    }
    return (Integer) subject.getPrincipal();
  }
}
