package com.mall.discover.persistence.dao.mysql;

import com.mall.discover.persistence.bo.ArticleBO;
import com.mall.discover.persistence.bo.ProductBO;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.persistence.entity.article.ArticleEntity;
import com.mall.discover.persistence.vo.ArticleVo;
import com.mall.discover.persistence.vo.ProductVo;
import com.mall.discover.persistence.vo.SubjectVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Repository
public interface ClientArticleMapper {

    /**
     * 获取文章列表
     */
    List<ArticleBO> queryArticleList(@Param("param") ArticleVo articleVo,
                                @Param("startRecord") int startRecord,
                                @Param("recordSize") int recordSize);
    /**
     * 获取文章列表
     */
    Integer queryArticleListCount(@Param("param") ArticleVo articleVo);

    /**
     * 获取文章详情
     */
    ArticleBO queryArticleInfo(@Param("param") ArticleVo articleVo);

    /**
     * 获取文章相关话题列表
     */
    List<SubjectBO> queryArticleSubjectPage(@Param("param") SubjectVo subjectVo);

    /**
     * 获取文章相关商品列表
     */
    List<ProductBO> queryArticleProductPage(@Param("param") ProductVo productVo);

    /**
     * 获取所有文章id集合
     */
    List<Long> queryAllArticleId();

    /**
     * 获取所有文章列表集合(包含删除后的)
     */
    List<ArticleBO> queryAllArticleList();

    /**
     * 批量更新文章Look的类型，仅一次
     */
    void batchUpdateArticleLookType(@Param("param")List<ArticleEntity> list);
}
