package com.mall.discover.persistence.bo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HistorySubjectBO {
    private Long subjectId;
    private String subjectName;
    private Long publishTime;
}
