package com.mall.discover.request.subject;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "话题名称搜索")
@Data
public class SubjectNameRequestDto extends CommonRequest {
    @ApiModelProperty(value = "话题名称")
    private String subjectName;
}
