package com.mall.discover.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: huangzhong
 * @Date: 2019/10/6
 * @Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "discover.params")
public class DiscoverProperties {
    /**
     * 话题名称模糊查询话题最大数量限制
     */
    private long querySubjectByName = 50;

    /**
     * 默认排序最大值（9999）
     */
    private long maxSort = 9999;

    /**
     * 客户端列表每页条数
     */
    private int clientPageSize = 10;

    /**
     * 客户端商品或话题内文章缓存最大页码
     */
    private int clientArticleMaxPage = 2;

    /**
     * 缓存详情最大页码
     */
    private int clientCacheInfoMaxPage = 6;

    /**
     * 客户端缓存过期时间（秒）
     */
    private long clientExpireTime = 5400;

    /**
     * 文章详情推荐下限
     */
    private int clientRecommendCount = 20;

    /**
     * 商品列表显示文章数量
     */
    private int productArticleCount = 5;

    /**
     * T台资源固定id
     */
    private Long definitionId = 64L;

    /**
     * T台话题id字段
     */
    private String subjectIdField = "subjectId";

    /**
     * T台资源排序字段
     */
    private String resourceSortField = "resourceSort";

    /**
     * T台资源位数量上限
     */
    private int resourceSortCount = 21;

    /**
     * h5链接域名
     */
    private String link = "";

    /**
     * 客户端默认商品（兼容老版本错误）
     */
    private String clientDefaultProductNo = "380881100005";
}
