package com.mall.discover.response.subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ArticleHistorySubjectResponse implements Serializable {
    private static final long serialVersionUID = -7176041323352926875L;
    private List<HistorySubject> historySubjectList;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistorySubject implements Serializable {
        private static final long serialVersionUID = -4115819542696753689L;
        private Long subjectId;
        private String subjectName;
        private Long publishTime;
    }
}
