package com.mall.discover.request.subject;

import com.doubo.common.model.request.PageQueryRequest;
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
@Data
@ApiModel(value = "话题内文章列表分页")
public class SubjectArticlePageRequestDto extends PageQueryRequest {
    private static final long serialVersionUID = -3026366730943245895L;

    @ApiModelProperty(value = "话题ID")
    @NotNull(message = "话题ID不能为空")
    @Min(value = 1, message = "话题ID不能小于0")
    private Long subjectId;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;


}
