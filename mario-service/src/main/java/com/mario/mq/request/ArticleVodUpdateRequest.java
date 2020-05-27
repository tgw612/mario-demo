package com.mario.mq.request;

import com.mall.discover.common.VodLook;
import java.io.Serializable;
import lombok.Data;

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
