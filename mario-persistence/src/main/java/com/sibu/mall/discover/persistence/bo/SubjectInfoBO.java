package com.mall.discover.persistence.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubjectInfoBO implements Serializable {
    private static final long serialVersionUID = 8653928969881584585L;
    private Long subjectId;

    private String subjectName;

    private String subjectStatus;

    private String publishTime;
}
