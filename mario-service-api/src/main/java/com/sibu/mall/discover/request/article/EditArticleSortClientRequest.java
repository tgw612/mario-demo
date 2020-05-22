package com.mall.discover.request.article;

import com.doubo.common.model.request.common.CommonRequest;
import com.mall.discover.common.article.DiscoverCountDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ApiModel("批量修改文章Client排序值")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EditArticleSortClientRequest extends CommonRequest {
    private static final long serialVersionUID = -8642677952217312953L;
    @ApiModelProperty(value = "修改排序值", notes = "")
    List<DiscoverCountDTO> bachList;
}
