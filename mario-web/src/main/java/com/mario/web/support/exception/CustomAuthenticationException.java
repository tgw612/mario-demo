package com.mario.web.support.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * User: qiujingwang Date: 2016/3/18 Time: 16:13
 */
public class CustomAuthenticationException extends Exception {

  /**
   * 重定向地址
   */
  @Getter
  @Setter
  private String redirectUrl;

  /**
   * 登录方式
   */
  @Getter
  @Setter
  private String loginType;

    /*public OssAuthenticationException(String msg) {
        super(msg);
    }*/

  public CustomAuthenticationException(String msg, String redirectUrl, String loginType) {
    super(msg);
    this.redirectUrl = redirectUrl;
    this.loginType = loginType;
  }


}
