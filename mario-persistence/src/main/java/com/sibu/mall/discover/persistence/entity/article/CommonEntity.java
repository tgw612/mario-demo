package com.mall.discover.persistence.entity.article;

import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tgw
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@Data
@KeySequence("SEQ_TEST")
public abstract class CommonEntity implements Serializable {
    private static final long serialVersionUID = 3470774269922222856L;

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


    @TableField("create_user_id")
    private String createUser;


}
