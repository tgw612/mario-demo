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
@ApiModel("话题内文章列表")
@Data
public class ClientSubjectArticlePageRequest extends PageQueryRequest {
    @ApiModelProperty(value = "话题ID", required = true)
    @Min(value = 1, message = "话题ID不能小于0")
    private long subjectId;
}
