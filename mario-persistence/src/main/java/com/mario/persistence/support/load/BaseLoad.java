package com.mario.persistence.support.load;

/**
 * Description: Author: 陈二伟 Date:2018/10/31
 */
public abstract class BaseLoad {

  /**
   * 初始化
   *
   * @throws Exception
   */
  public abstract void init() throws Exception;

  /**
   * 刷新
   *
   * @throws Exception
   */
  public abstract void refresh() throws Exception;
}
