package com.mall.discover.request.product;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("删除商品关联文章")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductDeleteArticleRequestDto extends CommonRequest {
    private static final long serialVersionUID = 5601451552212607048L;
    @NotNull(message = "文章id不能为空")
    @ApiModelProperty(value = "文章ID", notes = "", required = true)
    private String articleId;

    @NotNull(message = "商品id不能为空")
    @ApiModelProperty(value = "商品ID", notes = "", required = true)
    private String goodsId;
}
