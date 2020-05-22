package com.mall.discover.response.api;

import com.doubo.common.model.response.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Date;


@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class MemberPageQueryDiscoverListResponse extends CommonResponse {

    private static final long serialVersionUID = -3629870652053352388L;

    private String id;

    private Integer memberId;

    private String memberLogo;

    private String memberName;

    private String memberPhone;

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