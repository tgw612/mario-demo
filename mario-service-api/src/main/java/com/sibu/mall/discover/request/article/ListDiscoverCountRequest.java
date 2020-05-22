package com.mall.discover.request.article;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("文章列表查询")
@Data
@ToString
@EqualsAndHashCode
public class ListDiscoverCountRequest extends PageQueryRequest {
    @ApiModelProperty("1:文章 2:商品 3:话题")
    private Integer biztype;
}
