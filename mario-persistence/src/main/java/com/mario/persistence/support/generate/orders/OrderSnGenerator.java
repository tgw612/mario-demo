package com.mario.persistence.support.generate.orders;

/**
 * @author 陈志杭
 * @contact 279397942@qq.com
 * @date 2018/7/12
 * @description 生成订单号
 */
public interface OrderSnGenerator {

  /**
   * 生成主订单号 A + OrderCode +【两位32进制订单数据定位符，根据会员id定位】 例子：A201807061153000001FFFF
   *
   * @return
   */
  String genPsn(String memberID);

  /**
   * 生成子订单号 B+ OrderCode +【两位32进制订单数据定位符，根据会员id定位】 例子：B201807061153000001FFFF
   *
   * @return
   */
  String genSn(String memberID);


}
