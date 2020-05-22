package com.mall.discover.request.subject;

import com.doubo.common.model.request.PageQueryRequest;
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
@Data
@ApiModel(value = "删除话题文章关联关系")
public class SubjectArticleDeleteRequestDto extends PageQueryRequest {

    @ApiModelProperty(value = "话题ID")
    @NotNull(message = "话题ID不能为空")
    @Min(value = 1, message = "话题ID不能小于0")
    private Long subjectId;

    @ApiModelProperty(value = "文章ID")
    @NotNull(message = "文章ID不能为空")
    @Min(value = 1, message = "文章ID不能小于0")
    private Long articleId;
}
