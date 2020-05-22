package com.mall.discover.response.article;

import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.response.product.ProductInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class ArticleInfoResponse implements Serializable {
    private static final long serialVersionUID = 4632056541989732734L;
    @ApiModelProperty(value = "文章ID", notes = "")
    private String articleId;
    @ApiModelProperty(value = "文章状态", notes = "1-草稿 2-已发布")
    private Integer articleStatus;
    @ApiModelProperty(value = "创建时间", notes = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "文章排序值", notes = "")
    private Long sort;
    @ApiModelProperty(value = "文章标题最多30汉字", notes = "")
    private String articleTitle;
    @ApiModelProperty(value = "文章内容 文章内容太长，性能太低。优先级：低", notes = "文章内容太长，性能太低。优先级：低")
    private String articleContent;
    @ApiModelProperty(value = "图片url集合", notes = "")
    private ArticleLook pictureUrls;
    @ApiModelProperty(value = "话题名称", notes = "")
    private List<SubjectInfo> subject;
    @ApiModelProperty(value = "商品信息", notes = "")
    private List<ProductInfoResponse> productInfo;
    @ApiModelProperty(value = "文章发布时间")
    private Long publishTime;
    @Data
    public static class SubjectInfo implements Serializable{
        private static final long serialVersionUID = -4635753906130342182L;
        @ApiModelProperty(value = "话题ID")
        private Long subjectId;

        @ApiModelProperty(value = "话题名称")
        private String subjectName;

        @ApiModelProperty(value = "话题状态")
        private String subjectStatus;

        @ApiModelProperty(value = "话题发布时间")
        private String publishTime;
    }
}
