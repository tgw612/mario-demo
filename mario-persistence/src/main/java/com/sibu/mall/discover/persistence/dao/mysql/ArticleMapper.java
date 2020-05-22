package com.mall.discover.persistence.dao.mysql;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.discover.persistence.bo.*;
import com.mall.discover.persistence.entity.article.ArticleEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("articleMapper")
public interface ArticleMapper extends BaseMapper<ArticleEntity> {

    @Update("update discover_article set is_deleted=1 where article_id=#{id}")
    void deleteArticleById(@Param("id") Long id);


    List<ArticleListBO> listArticle(@Param("param") ListArticleRequestBO request,
                                    @Param("startRecord") int startRecord,
                                    @Param("recordSize") int recordSize);

    int listArticleCount(@Param("param") ListArticleRequestBO request);

    @Select("select da.article_id,da.article_content,da.article_title,da.article_look,da.article_status,da.publish_time,dc.sort " +
            "FROM discover_article as da LEFT JOIN discover_count dc on da.article_id=dc.bizId " +
            "where da.article_id=#{id} and da.is_deleted=0 and dc.is_deleted=0 and dc.biztype=1")
    ArticleInfoBO getArticleInfo(@Param("id") Long articleId);


    @Options(useGeneratedKeys = true, keyProperty = "articleId", keyColumn = "article_id")
    @Insert("insert into discover_article(article_status,article_title,article_content,article_look,article_look_type,is_deleted,create_user_id," +
            "update_time,create_time,publish_time,create_user_type) " +
            "values(#{articleStatus},#{articleTitle},#{articleContent},#{articleLook},#{articleLookType},#{status},#{createUser}," +
            "#{updateTime},#{createTime},#{publishTime},#{createUserType})")
    void saveArticleEntity(ArticleEntity entity);

    @Select("select ds.subject_id,ds.subject_name,ds.publish_time,ds.subject_status FROM discover_article as da LEFT JOIN discover_relation as dr ON da.article_id=dr.to_id " +
            "LEFT JOIN discover_subject ds on dr.from_id=ds.subject_id where da.article_id=#{id} and dr.is_deleted=0 and ds.is_deleted=0 and dr.biztype=#{biztype}")
    List<SubjectInfoBO> getRelateSubjectName(@Param("id") Long articleId, @Param("biztype") Integer biztype);

    @Select("select dp.product_id,dp.product_name FROM discover_article as da LEFT JOIN discover_relation as dr ON da.article_id=dr.to_id " +
            "LEFT JOIN discover_product dp on dr.from_id=dp.product_id where da.article_id=#{articleId} and dr.is_deleted=0 and dp.is_deleted =0 and dr.biztype=#{biztype}")
    List<ProductIdNameBO> getArticleRelateProduct(@Param("articleId") Long articleId, @Param("biztype") Integer biztype);

    @Update("update discover_relation set is_deleted=1 where to_id=#{id} and biztype in(1,2)")
    void deleteRelation(@Param("id") Long articleId);

    @Select("select from_id FROM discover_relation as dr where to_id=#{id} and is_deleted=0 and dr.biztype=1")
    List<Long> getRelateProduct(@Param("id") Long articleId);


    @Select("select count(article_id) from discover_article where publish_time <#{current} and article_status=3 and is_deleted=0")
    Long selectWaitPublishCount(@Param("current") long current);

    @Select("select article_id,article_status,update_time,is_deleted as status,publish_time" +
            " from discover_article where article_status=3 and is_deleted=0 and publish_time <#{current} LIMIT #{startRecord},#{recordSize}")
    List<WaitPublishArticleBO> selectWaitPublishList(@Param("current") long current,
                                                     @Param("startRecord") int startRecord,
                                                     @Param("recordSize") int recordSize);

    void updateArticleStatusBatch(@Param("param") List<WaitPublishArticleBO> waitPublishArticles);

    @Select("select article_look from discover_article where article_id=#{id}")
    String selectArticleLook(@Param("id") Long articleId);

    // TODO: 2019/10/31 超慢查询 待改善  建立articleId与fileId关系表
    @Select("select article_id from discover_article where article_look like CONCAT('%',#{fileId},'%')")
    Long selectLikeArticleLook(@Param("fileId") String fileId);

    @Update("update discover_article set article_look=#{look} where article_id=#{id}")
    void updateArticleLook(@Param("id") Long articleId,
                           @Param("look") String look);


}
