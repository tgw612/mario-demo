package com.mall.discover.request.api;

import com.doubo.common.model.request.PageQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class MemberPageQueryDiscoverListRequest extends PageQueryRequest {

    private static final long serialVersionUID = -2704590696114704047L;

    private Integer memberId;

    private String keyWords;

    private String cateId;

    private String productId;

}