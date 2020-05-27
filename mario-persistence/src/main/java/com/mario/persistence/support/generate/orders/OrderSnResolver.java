package com.mario.persistence.support.generate.orders;

/**
 * Description:订单号解析 Author: 陈二伟 Date: 2018/10/09
 */
public interface OrderSnResolver {

  /**
   * 根据 主子订单号 获取 定位索引
   *
   * @param psnOrSn
   * @return
   */
  Integer resolveLocateIndexByPsnOrSn(String psnOrSn);


  /**
   * 根据会员ID 获取定位索引
   *
   * @param memberID
   * @return
   */
  Integer resolveLocateIndexByMemberID(String memberID);

  /**
   * 解析旧子订单号 对应新子订单号
   *
   * @param orderSn
   * @return
   */
  String resolveOldOrderSn(String orderSn);

  /**
   * 解析旧主订单号 对应新主订单号
   *
   * @param orderPSn
   * @return
   */
  String resolveOldOrderPSn(String orderPSn);

  /**
   * 根据memberId获取新订单号
   *
   * @param orderSnOrPsn
   * @param memberId
   * @return
   */
  String resolveOldOrderSnOrPsn(String orderSnOrPsn, String memberId);


}
