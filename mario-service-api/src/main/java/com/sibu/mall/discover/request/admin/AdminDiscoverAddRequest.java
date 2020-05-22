package com.mall.discover.request.admin;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AdminDiscoverAddRequest extends CommonRequest {
    private String id;

    private Integer memberId;

    private String  memberLogo;

    private String  memberName;

    private String  memberPhone;

    private String cateId;

    private String  cateName;

    private String title;

    private String  content;

    private String  pictures;

    private String  thumbnailSmall;

    private String  video;

    private Integer videoHeight;

    private Integer videoWidth;

    private Integer productId;

    private Boolean sameProductFlag = false;

    private Integer sameProductId;

    private Integer order;

    private  boolean display;
}
