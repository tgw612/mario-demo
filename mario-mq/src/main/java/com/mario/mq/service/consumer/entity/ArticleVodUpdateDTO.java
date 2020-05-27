package com.mario.mq.service.consumer.entity;

import com.mall.discover.common.VodLook;
import java.io.Serializable;
import lombok.Data;

@Data
public class ArticleVodUpdateDTO implements Serializable {

  private static final long serialVersionUID = -5537031074437280246L;
  private Long articleId;
  private VodLook vodLook;

  public ArticleVodUpdateDTO() {
  }

  public ArticleVodUpdateDTO(Long articleId, VodLook vodLook) {
    this.articleId = articleId;
    this.vodLook = vodLook;
  }
}
