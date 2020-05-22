package com.mall.discover.request.client;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel("商品内文章列表")
@Data
public class ClientProductArticlePageRequest extends PageQueryRequest {
    @ApiModelProperty(value = "商品id", required = true)
    private long productId;

    @ApiModelProperty(value = "商品编号", notes = "2.0以后版本需要")
    private String productNo;
}
