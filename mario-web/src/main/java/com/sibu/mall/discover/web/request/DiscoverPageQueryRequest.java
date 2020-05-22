package com.mall.discover.web.request;

import com.doubo.common.model.request.PageQueryRequest;
import lombok.Data;

@Data
public class DiscoverPageQueryRequest extends PageQueryRequest {

    /**
     * 分类id
     */
    private String cateId;

    /**
     * 商品id
     */
    private String productId;
}
