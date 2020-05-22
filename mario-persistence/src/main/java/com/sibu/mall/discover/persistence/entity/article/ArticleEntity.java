package com.mall.discover.persistence.entity.article;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("discover_article")
public class ArticleEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = -7972175900082474463L;

    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;
    /**
     * '文章状态：1-草稿 2-已发布',
     */
    @TableField("article_status")
    private Integer articleStatus;
    /**
     *
     */
    @TableField("article_title")
    private String articleTitle;
    /**
     * 文章内容
     */
    @TableField("article_content")
    private String articleContent;
    /**
     * 包含文章照片URL等信息
     */
    @TableField("article_look")
    private String articleLook;

    /**
     * look类型：仅图片、仅视频等
     */
    @TableField("article_look_type")
    private Integer articleLookType;

    @TableField("publish_time")
    private Long publishTime;

    @TableField("create_user_type")
    private Integer createUserType;
}
