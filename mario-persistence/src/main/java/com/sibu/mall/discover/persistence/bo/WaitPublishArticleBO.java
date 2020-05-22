package com.mall.discover.persistence.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WaitPublishArticleBO implements Serializable {
    private static final long serialVersionUID = 3286189294459577312L;

    private Long articleId;
    private Integer articleStatus;
    private Long updateTime;
    private Integer status;
    private Long publishTime;

}
