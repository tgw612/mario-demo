package com.mall.discover.request.admin;

import com.doubo.common.model.request.PageQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AdminDiscoverListCateRequest extends PageQueryRequest {

    private String cateName;

    private String cateId;

    private String content;
}
