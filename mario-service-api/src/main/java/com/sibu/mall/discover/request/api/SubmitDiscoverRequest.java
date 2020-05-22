package com.mall.discover.request.api;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * author:wang.zhiliang
 * date:2018.11.22
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SubmitDiscoverRequest extends CommonRequest implements Serializable{

    private String id;

    private Integer memberId;

    private String  memberLogo;

    private String  memberName;

    private String  memberPhone;

    private String cateId;

    private String  cateName;

    private String  content;

    private String  pictures;

    private String  thumbnailSmall;

    private String  video;

    private Integer videoHeight;

    private Integer videoWidth;

    private Integer productId;

    private Boolean sameProductFlag = false;

    private Integer sameProductId;

}
