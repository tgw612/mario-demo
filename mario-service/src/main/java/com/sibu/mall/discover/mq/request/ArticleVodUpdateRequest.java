package com.mall.discover.mq.request;

import com.mall.discover.common.VodLook;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class ArticleVodUpdateRequest implements Serializable {
    private static final long serialVersionUID = -5537031074437280246L;
    private Long articleId;
    private VodLook vodLook;

    public ArticleVodUpdateRequest() {
    }

    public ArticleVodUpdateRequest(Long articleId, VodLook vodLook) {
        this.articleId = articleId;
        this.vodLook = vodLook;
    }
}
