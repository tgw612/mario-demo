package com.mall.discover.request.article;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("编辑话题/商品内文章列表排序")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EditRelateArticleSortRequest extends CommonRequest {
    @ApiModelProperty(value = "关联ID", notes = "")
    private Long relationId;
    @ApiModelProperty(value = "排序值", notes = "")
    private long sort;

}
