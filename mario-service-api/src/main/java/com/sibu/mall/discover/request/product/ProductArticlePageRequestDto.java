package com.mall.discover.request.product;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("商品内文章列表查询")
@Data
@ToString
@EqualsAndHashCode
public class ProductArticlePageRequestDto extends PageQueryRequest {
    private static final long serialVersionUID = 3868519128547520757L;
    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "文章内容", notes = "模糊查询 TODO @技术 文章内容太长，性能太低。优先级：低")
    private String articleContent;

    @ApiModelProperty(value = "商品ID", required = true)
    private Long productId;
}
