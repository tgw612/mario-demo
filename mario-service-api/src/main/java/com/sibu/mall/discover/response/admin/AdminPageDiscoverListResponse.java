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
public class AdminPageDiscoverListResponse implements Serializable {

    private static final long serialVersionUID = 8690916214887215823L;

    private String id;

    private String title;
    private String content;

    private Integer memberId;
    private String memberLogo;
    private String memberName;

    private String cateId;
    private String  cateName;

    private Integer order;

    private Boolean display;

    private Integer forwards; //分享次数

    private Date createTime;
}
