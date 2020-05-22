package com.mall.discover.web.request;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetShareCountRequest extends CommonRequest {

    /**
     * 商品id
     */
    @NotNull
    private String productId;
}
