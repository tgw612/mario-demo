package com.mario.persistence.support.generate.orders.impl;


import com.doubo.common.interfaces.StringHashCoding;
import com.mall.common.utils.SeqGenUtil;
import com.mario.persistence.support.generate.orders.OrderSnGenerator;
import com.mario.persistence.support.generate.orders.OrderSnResolver;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:分布式订单号生成
 * <p>
 * Author: 陈二伟 Date: 2018/10/09
 */
//@Log4j
public class DistributeOrderSnGeneratorImpl implements OrderSnGenerator, OrderSnResolver {

  @Getter
  @Setter
  private StringHashCoding hashCoding;

  public static void main(String args[]) {
    if (StringUtils.isBlank("A1810161406291221184740JV")) {
      throw new RuntimeException("通过 psnOrSn 解析定位索引时，不能为空.");
    }
    //要考虑 不足两位时，补位的左边 补位的0
    String localIndex = "A1810161406291221184740JV"
        .substring("A1810161406291221184740JV".length() - 2);
    String left = localIndex.substring(0, 1);
    String right = localIndex.substring(1);

    if ("0".equals(left)) {
      localIndex = right;
    }
    System.out.print(Integer.parseInt(localIndex, 32));
  }

  /**
   * 生成主订单号 A + OrderCode +【两位32进制订单数据定位符，根据会员id定位】 例子：A201807061153000001FFFF
   *
   * @return
   */
  @Override
  public String genPsn(String memberID) {
    return this.gen("A", memberID);

  }

  /**
   * 生成子订单号 B+ OrderCode +【两位32进制订单数据定位符，根据会员id定位】 例子：B201807061153000001FFFF
   *
   * @return
   */
  @Override
  public String genSn(String memberID) {
    return this.gen("B", memberID);
  }

  /**
   * 生成订单号 例子：
   *
   * @return
   */
  private String gen(String prefix, String memberUID) {
    String orderTableIndex = this.resolveOrderTableIndex(memberUID);
    String id = prefix + SeqGenUtil.getOrderCode() + orderTableIndex;
    return id;
  }

  /**
   * 一致性hash算法 两位32进制订单数据定位符，根据会员id定位
   *
   * @param memberId
   * @return
   */
  public String resolveOrderTableIndex(String memberId) {
    String orderTableIndex = Integer.toString(this.hashCoding.hashFor(memberId), 32).toUpperCase();
    if (orderTableIndex.length() == 1) {
      orderTableIndex = "0" + orderTableIndex;
    }
    return orderTableIndex;
  }

  @Override
  public Integer resolveLocateIndexByPsnOrSn(String psnOrSn) {
    if (StringUtils.isBlank(psnOrSn)) {
      throw new RuntimeException("通过 psnOrSn 解析定位索引时，不能为空.");
    }
    //要考虑 不足两位时，补位的左边 补位的0
    String localIndex = psnOrSn.substring(psnOrSn.length() - 2);
    String left = localIndex.substring(0, 1);
    String right = localIndex.substring(1);

    if ("0".equals(left)) {
      localIndex = right;
    }
    return Integer.parseInt(localIndex, 32);
  }

  @Override
  public Integer resolveLocateIndexByMemberID(String memberID) {
    return this.hashCoding.hashFor(memberID);
  }

  @Override
  public String resolveOldOrderSn(String orderSn) {
    return null;
  }

  @Override
  public String resolveOldOrderPSn(String orderPSn) {
    return null;
  }

  @Override
  public String resolveOldOrderSnOrPsn(String orderSnOrPsn, String memberId) {
    if (StringUtils.isBlank(memberId)) {
      return orderSnOrPsn;
    }
    return orderSnOrPsn + resolveOrderTableIndex(memberId);
  }
}

