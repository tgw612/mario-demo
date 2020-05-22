package com.mall.discover.persistence.dao.mysql;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.discover.persistence.bo.CountReadShareUpdateBO;
import com.mall.discover.persistence.bo.DiscoverCountBO;
import com.mall.discover.persistence.entity.article.ArticleCountEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CountMapper extends BaseMapper<ArticleCountEntity> {


    void editArticleNums(List<ArticleCountEntity> infos);

    @Update("update discover_count set is_deleted=1 where bizId=#{id} and biztype=1")
    void deleteArticleCount(@Param("id") Long articleId);

    void editDiscoverCountSortClient(@Param("param") List<DiscoverCountBO> bachList);

    @Select("select id,bizId as bizId,read_count,share_count,sort_client,sort,biztype,create_time " +
            "from discover_count where biztype=#{biztype} and is_deleted=0 limit #{startRecord},#{recordSize}")
    List<ArticleCountEntity> queryPageList(@Param("biztype") Integer biztype,
                                           @Param("startRecord") int startRecord,
                                           @Param("recordSize") int recordSize);

    @Select("select count(id) from discover_count where biztype=#{biztype} and is_deleted= 0")
    int countByType(@Param("biztype") Integer biztype);

    @Update("update discover_count set sort=#{sort} where bizId=#{bizid} and biztype=#{biztype}")
    void editSort(@Param("biztype") Integer biztype,
                  @Param("sort") Long sort,
                  @Param("bizid") Long bizid);

    void batchUpate(@Param("param") List<CountReadShareUpdateBO> bo);

    @Select("select * from discover_count where biztype=1 and bizId=#{bizid} and is_deleted=0")
    ArticleCountEntity selectArticle(@Param("bizid") Long bizid);

    @Select("select count(id) from discover_count where is_deleted=0")
    Long countAllExist();

    @Select("select id,bizId as bizId,biztype,read_count,share_count " +
            "from discover_count where is_deleted=0 limit #{startRecord},#{recordSize}")
    List<CountReadShareUpdateBO> queryCountPageList(@Param("startRecord") int startRecord,
                                                    @Param("recordSize") int recordSize);

}
