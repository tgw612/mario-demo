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
@TableName("discover_relation")
public class RelationEntity extends CommonEntity implements Serializable {
    private static final long serialVersionUID = 2014764067329788193L;
    @TableId(value = "id")
    private Long id;
    /**
     * GOODS_ARTICLE(1,"商品-文章"),
     * SUBJECT_ARTICLE(2,"话题-文章"),
     * GOODS_SUBJECT(3,"商品-话题");
     */
    @TableField("biztype")
    private Integer biztype;
    /**
     * 来源id
     */
    @TableField("from_id")
    private Long fromId;
    /**
     * 目标id
     */
    @TableField("to_id")
    private Long toId;

    @TableField("sort")
    private Long sort;
}
