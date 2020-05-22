package com.mall.discover.request.product;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;

@Data
public class ProductESSearchRequestDto extends CommonRequest {
    private static final long serialVersionUID = 1355879811880873688L;
    private String pid;
}
