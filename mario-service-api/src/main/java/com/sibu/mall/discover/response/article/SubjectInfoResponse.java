package com.mall.discover.response.article;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubjectInfoResponse implements Serializable {
    private static final long serialVersionUID = 8241394708350861434L;
    private Long subjectId;

    private String subjectName;
}
