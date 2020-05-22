package com.mall.discover.response.admin;

import com.doubo.common.model.response.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class AdminDiscoverDetailResponse implements Serializable {
    private static final long serialVersionUID = -5917669620465427176L;


    private String id;


    private Integer memberId;

    private String memberLogo;

    private String memberName;

    private String memberPhone;

    private String cateId;

    private String cateName;

    private String title;

    private String content;

    private String pictures;

    private String thumbnailSmall;


    private String video;


    private Integer videoHeight;


    private Integer videoWidth;


    private Integer productId;


    private Boolean recomend;


    private Boolean display;


    private Integer views;


    private Integer forwards;


    private Date createTime;


    private Date updateTime;


    private Boolean sameProductFlag;


    private Integer sameProductId;


    private Integer order;

}
