package com.mall.discover.request.article;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("删除文章")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteArticleRequest extends CommonRequest {
    private static final long serialVersionUID = -4948843252664258647L;
    @NotNull(message = "文章主键不能为空")
    @ApiModelProperty(value = "文章ID", notes = "物理删除所有关联表关系，文章表为逻辑删除")
    private Long articleId;
}
