package com.mario.persistence.vo;

import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/10/9
 * @Description:
 */
@Data
public class ProductVo extends BaseVo {

  private Long productId;
  /**
   * 商品编号
   */
  private String productNo;

  private Long articleId;
  /**
   * 通用类型
   */
  private Integer bizType;

  /**
   * 是否是爆款：0-非爆款  1-爆款
   */
  private Integer hotProductStatus;

  /**
   * 是否是高佣金：0-非  1-高佣金
   */
  private Integer highCommissionStatus;

  /**
   * 通用关联关系
   */
  private Integer relation;
}
