package com.mall.discover.mq.service.consumer.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class PromotionActivityMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    private String actId;

    /**
     * 活动枚举：3秒杀，4拼团
     */
    private Integer actEnumCode;

    /**
     * 时间戳版本号
     */
    private String version;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 商品id：actRange为1时比传
     */
    private List<Integer> productIds;

    /**
     * 貨品id actRange为1时比传
     */
    private List<Integer> productGoodsIds;

    /**
     * 0 全部； 1：部分
     */
    private Integer actRange;
}
