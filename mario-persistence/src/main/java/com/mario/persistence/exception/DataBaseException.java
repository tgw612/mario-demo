package com.mario.persistence.exception;

import org.springframework.dao.DataAccessException;

/**
 * ClassName 数据库异常类 Author qiujingwang Date 2017/3/14
 */
public class DataBaseException extends DataAccessException {

  private static final long serialVersionUID = -4405520539628101866L;

  public DataBaseException(String message) {
    super(message);
  }

  public DataBaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
