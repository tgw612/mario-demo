package com.mall.discover.mq.service.consumer.entity;

import lombok.Data;

@Data
public class PaymentSuccessEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 直播间ID
     */
    private String liveId;
}
