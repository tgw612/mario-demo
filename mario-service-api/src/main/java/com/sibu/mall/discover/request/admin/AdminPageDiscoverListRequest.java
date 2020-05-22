package com.mall.discover.request.admin;

import com.doubo.common.model.request.PageQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class AdminPageDiscoverListRequest extends PageQueryRequest {

    private String cateName; //分类名称

    private String cateId;

    private String content;

    private String title;
}
