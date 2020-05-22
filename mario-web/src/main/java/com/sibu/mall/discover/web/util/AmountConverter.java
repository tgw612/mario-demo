package com.mall.discover.web.util;

import java.math.BigDecimal;

/**
 * 金额转换
 *
 * @author taowei
 */

public class AmountConverter {

    public static String bigDecimalToString(BigDecimal bigDecimal, Integer scale) {
        if (bigDecimal == null) {
            return null;
        }
        return bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).toString();
    }

    public static String bigDecimalToString(BigDecimal bigDecimal) {
        return bigDecimalToString(bigDecimal, 2);
    }

    public static String cent2YuanString(Long cent) {
        BigDecimal yuan = cent2Yuan(cent);
        return bigDecimalToString(yuan);
    }

    public static BigDecimal cent2Yuan(Long cent) {
        if (cent == null) {
            return null;
        }
        BigDecimal bigCent = new BigDecimal(cent);
        return bigCent.divide(new BigDecimal(100));
    }

    public static BigDecimal toDecimalRate(Integer rate) {
        return new BigDecimal(rate).divide(new BigDecimal(100000));
    }

    /**
     * 计算佣金金额
     */
    public static BigDecimal getCommissionFee(BigDecimal centPrice, Integer rate) {
        if (centPrice == null || rate == null) {
            return null;
        }
        return centPrice.multiply(
            new BigDecimal(rate).divide(
                    new BigDecimal(100000)
            ));
    }
}
