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
public class ListArticleRequest extends PageQueryRequest {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "排序顺序, asc desc,默认asc")
    private String sortOrder;
    @ApiModelProperty(value = "排序字段1.发布状态 2.创建时间 3.id 如果以上字段相同 则以创建时间desc排序 ")
    private String sortField;
    @ApiModelProperty(value = "文章ID", notes = "")
    private String articleId;
    @ApiModelProperty(value = "文章内容", notes = "文章内容太长，性能太低。优先级：低")
    private String articleContent;
}
