package com.mall.discover.persistence.entity.article;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@Data
@TableName("discover_count")
public class ArticleCountEntity  implements Serializable {

    private static final long serialVersionUID = -4415516005313295906L;
    @TableId(value = "id")
    private Long id;

    @TableField("bizId")
    private Long bizId;
    /**
     * 阅读次数
     */
    @TableField("read_count")
    private Long readCount;
    /**
     * 分享次数
     */
    @TableField("share_count")
    private Long shareCount;
    /**
     * 客户端 文章流中的排序(X值)
     */
    @TableField("sort_client")
    private Long sortClient;

    /**
     * 排序
     */
    @TableField("sort")
    private Long sort;

    /**
     *1:文章 2:商品 3:话题
     */
    @TableField("biztype")
    private Integer biztype;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Long updateTime;
    /**
     * 是否删除：默认为0，0-正常 1-已删除
     */
    @TableField("is_deleted")
    private Integer status;
}
