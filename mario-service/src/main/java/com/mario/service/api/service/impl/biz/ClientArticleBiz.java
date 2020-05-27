package com.mario.service.api.service.impl.biz;

import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.persistence.bo.ArticleBO;
import com.mall.discover.persistence.bo.ProductBO;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.persistence.dao.mysql.ClientArticleMapper;
import com.mall.discover.persistence.entity.article.ArticleEntity;
import com.mall.discover.persistence.vo.ArticleVo;
import com.mall.discover.persistence.vo.ProductVo;
import com.mall.discover.persistence.vo.SubjectVo;
import com.mall.discover.request.client.ClientArticleInfoRequest;
import com.mall.discover.request.client.ClientArticlePageRequest;
import com.mario.service.api.config.properties.DiscoverProperties;
import com.mario.service.api.util.DiscoverUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Slf4j
@Service
public class ClientArticleBiz {

  @Autowired
  private ClientArticleMapper clientArticleMapper;

  @Autowired
  private DiscoverProperties discoverProperties;

  /**
   * 获取文章列表
   */
  public List<ArticleBO> queryArticlePage(ClientArticlePageRequest request,
      List<Integer> articleLookTypeList) {
    //固定每页大小
    int clientPageSize = discoverProperties.getClientPageSize();
    request.setPageSize(clientPageSize);
    ArticleVo articleVo = DiscoverUtil.getArticleVo();
    if (!ObjectUtils.isEmpty(articleLookTypeList)) {
      articleVo.setLookTypeList(articleLookTypeList);
    }

    //查询文章列表
    return clientArticleMapper
        .queryArticleList(articleVo, PageUtils.getStartPage(request), request.getPageSize());
  }

  /**
   * 获取文章列表数量
   */
  public Integer queryArticlePageCount(List<Integer> articleLookTypeList) {
    ArticleVo articleVo = DiscoverUtil.getArticleVo();
    if (!ObjectUtils.isEmpty(articleLookTypeList)) {
      articleVo.setLookTypeList(articleLookTypeList);
    }

    return clientArticleMapper.queryArticleListCount(articleVo);
  }

  /**
   * 获取文章相关话题
   */
  public List<SubjectBO> queryArticleSubject(Long articleId) {
    //查询相关话题列表
    SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
    subjectVo.setArticleId(articleId);
    return clientArticleMapper.queryArticleSubjectPage(subjectVo);
  }

  /**
   * 获取文章相关商品
   */
  public List<ProductBO> queryArticleProduct(Long articleId) {
    //查询相关商品列表
    ProductVo productVo = DiscoverUtil.getProductVo();
    productVo.setArticleId(articleId);
    return clientArticleMapper.queryArticleProductPage(productVo);
  }

  /**
   * 获取文章详情
   */
  public ArticleBO queryArticleInfo(ClientArticleInfoRequest request) {
    ArticleVo articleVo = DiscoverUtil.getArticleVo();
    articleVo.setArticleId(request.getArticleId());
    return clientArticleMapper.queryArticleInfo(articleVo);
  }

  /**
   * 批量更新文章Look的类型(仅一次)
   */
  public boolean updateArticleLookType() {
    List<ArticleBO> articleBOS = clientArticleMapper.queryAllArticleList();
    List<ArticleEntity> list = new ArrayList<>();
    for (ArticleBO articleBO : articleBOS) {
      ArticleEntity entity = new ArticleEntity();
      entity.setArticleId(articleBO.getArticleId());
      ArticleLook articleLook = DiscoverUtil
          .convertStringResponse(articleBO.getArticleLook(), ArticleLook.class);
      entity.setArticleLookType(DiscoverUtil.getArticleLookType(articleLook));
      list.add(entity);
    }
    clientArticleMapper.batchUpdateArticleLookType(list);
    return true;
  }
}
