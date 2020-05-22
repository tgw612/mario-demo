package com.mall.discover.response.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/9/29
 * @Description:
 */
@Data
@ApiModel(value = "商品详情")
public class ClientProductInfoResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;
    @ApiModelProperty(value = "商品id")
    private long productId;

    @ApiModelProperty(value = "商品编号")
    private String productNo;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片URL")
    private List<String> productUrlList;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "销量")
    private long mallCount;

    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;

}
