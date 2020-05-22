package com.mall.discover.response.client;

import com.mall.discover.common.article.ArticleLook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@Data
@ApiModel(value = "文章详情")
public class ClientArticleInfoResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "图片集合")
    private ArticleLook articleLook;

    @ApiModelProperty(value = "阅读次数")
    private long readCount;

    @ApiModelProperty(value = "阅读次数")
    private String viewCountString;

    @ApiModelProperty(value = "分享赚总金额")
    private BigDecimal totalCommission;

    @ApiModelProperty(value = "点赞信息")
    private ClientLikeResponse clientLikeResponse;

    @ApiModelProperty(value = "话题集合")
    private List<ClientSubjectResponse> subjectList;

    @ApiModelProperty(value = "商品信息集合")
    private List<ClientProductResponse> productList;

    @ApiModelProperty(value = "相关文章推荐")
    private List<ClientArticleResponse> articleList;
}
