package com.mall.discover.persistence.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WaitPublishSubjectBO implements Serializable {
    private static final long serialVersionUID = 3286189294459577312L;

    private Long subjectId;
    private Long updateTime;
    private Integer subjectStatus;
    private Long publishTime;
    private Integer deleted;

}
