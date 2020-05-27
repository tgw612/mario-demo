package com.mario.service.api.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.mario.service.api.config.properties.DiscoverProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: huangzhong
 * @Date: 2019/10/6
 * @Description: 动态更改发现页配置参数
 */
@Component
@Slf4j
public class DiscoverConfig {

  /**
   * 话题名称模糊查询话题最大数量限制
   */
  private static String QUERY_SUBJECT_BY_NAME = "discover.params.querySubjectByName";
  /**
   * 默认排序最大值（9999）
   */
  private static String MAX_SORT = "discover.params.maxSort";
  /**
   * 客户端列表每页条数
   */
  private static String CLIENT_PAGE_SIZE = "discover.params.clientPageSize";
  /**
   * 客户端商品或话题内文章缓存最大页码
   */
  private static String CLIENT_ARTICLE_MAX_PAGE = "discover.params.clientArticleMaxPage";
  /**
   * 客户端缓存过期时间（秒）
   */
  private static String CLIENT_EXPIRE_TIME = "discover.params.clientExpireTime";
  /**
   * 文章详情推荐下限
   */
  private static String CLIENT_RECOMMEND_COUNT = "discover.params.clientRecommendCount";
  /**
   * T台资源固定id
   */
  private static String DEFINITION_ID = "discover.params.definitionId";
  /**
   * h5链接域名
   */
  private static String LINK = "discover.params.link";

  /**
   * 客户端默认商品（兼容老版本错误）
   */
  private static String DEFAULT_PRODUCT_NO = "discover.params.clientDefaultProductNo";

  @Autowired
  private DiscoverProperties discoverProperties;

  @ApolloConfig
  private Config config;

  @ApolloConfigChangeListener()
  private void onChange(ConfigChangeEvent changeEvent) {
    //话题名称模糊查询话题最大数量限制
    String querySubjectByName = config.getProperty(QUERY_SUBJECT_BY_NAME, "50");
    //默认排序最大值（9999）
    String maxSort = config.getProperty(MAX_SORT, "9999");
    //客户端列表每页条数
    String clientPageSize = config.getProperty(CLIENT_PAGE_SIZE, "10");
    //客户端商品或话题内文章缓存最大页码
    String clientArticleMaxPage = config.getProperty(CLIENT_ARTICLE_MAX_PAGE, "2");
    //客户端缓存过期时间（秒）
    String clientExpireTime = config.getProperty(CLIENT_EXPIRE_TIME, "5400");
    //文章推荐下限
    String clientRecommendCount = config.getProperty(CLIENT_RECOMMEND_COUNT, "20");
    //T台资源固定id
    String definitionId = config.getProperty(DEFINITION_ID, "64");
    //h5链接地址
    String link = config.getProperty(LINK, "www.wljs.com");
    //客户端默认商品（兼容老版本错误）
    String defaultProductNo = config.getProperty(DEFAULT_PRODUCT_NO, "380881100005");

    discoverProperties.setMaxSort(Long.valueOf(maxSort));
    discoverProperties.setQuerySubjectByName(Long.valueOf(querySubjectByName));
    discoverProperties.setClientPageSize(Integer.valueOf(clientPageSize));
    discoverProperties.setClientArticleMaxPage(Integer.valueOf(clientArticleMaxPage));
    discoverProperties.setClientExpireTime(Long.valueOf(clientExpireTime));
    discoverProperties.setClientRecommendCount(Integer.valueOf(clientRecommendCount));
    discoverProperties.setDefinitionId(Long.valueOf(definitionId));
    discoverProperties.setLink(link);
    discoverProperties.setClientDefaultProductNo(defaultProductNo);
  }
}
