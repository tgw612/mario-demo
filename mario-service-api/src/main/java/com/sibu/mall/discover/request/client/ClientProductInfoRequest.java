package com.mall.discover.request.client;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "商品详情")
@Data
public class ClientProductInfoRequest extends CommonRequest {

    @ApiModelProperty(value = "商品id")
    private long productId;

    @ApiModelProperty(value = "商品编号", notes = "2.0以后版本需要")
    private String productNo;
}
