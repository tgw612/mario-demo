package com.mario.web.common.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA. User: qiujingwang Date: 2017/4/7 Description:
 */
public interface Cookie {

  /**
   * The value of deleted cookie (with the maxAge 0).
   */
  public static final String DELETED_COOKIE_VALUE = "deleteMe";


  /**
   * The number of seconds in one year (= 60 * 60 * 24 * 365).
   */
  public static final int ONE_YEAR = 60 * 60 * 24 * 365;

  /**
   * Root path to use when the path hasn't been set and request context root is empty or null.
   */
  public static final String ROOT_PATH = "/";

  String getName();

  void setName(String name);

  String getValue();

  void setValue(String value);

    /*String getUidName();

    void setUidName(String uidName);

    String getUidValue();

    void setUidValue(String uidValue);*/

  String getComment();

  void setComment(String comment);

  String getDomain();

  void setDomain(String domain);

  int getMaxAge();

  void setMaxAge(int maxAge);

  String getPath();

  void setPath(String path);

  boolean isSecure();

  void setSecure(boolean secure);

  int getVersion();

  void setVersion(int version);

  boolean isHttpOnly();

  void setHttpOnly(boolean httpOnly);

  void saveTo(HttpServletRequest request, HttpServletResponse response);

  void removeFrom(HttpServletRequest request, HttpServletResponse response);

  String readValue(HttpServletRequest request, HttpServletResponse response);

  /*Long readUidValue(HttpServletRequest request, HttpServletResponse response);*/
}

