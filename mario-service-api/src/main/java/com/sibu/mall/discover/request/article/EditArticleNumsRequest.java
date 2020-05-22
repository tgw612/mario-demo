package com.mall.discover.request.article;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ApiModel("批量修改文章阅读数/分享数")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EditArticleNumsRequest extends CommonRequest {
    private static final long serialVersionUID = -8642677952217312953L;
    @ApiModelProperty(value = "修改文章信息详情", notes = "")
    List<ArticleNumsInfo> infos;

    @Data
    @ToString(callSuper = true)
    public static class ArticleNumsInfo {
        @ApiModelProperty(value = "文章ID", notes = "")
        private String articleId;
        @ApiModelProperty(value = "阅读数", notes = "")
        private Long readNums;
        @ApiModelProperty(value = "分享数", notes = "")
        private Long shareNums;
        @ApiModelProperty(value = "文章排序值", notes = "")
        private Long articleSort;
    }
}
