package com.mall.discover.response.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class MemberDiscoverDetailResponse implements Serializable {

    private String id;

    private Integer memberId;

    private String memberLogo;

    private String memberName;

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

}