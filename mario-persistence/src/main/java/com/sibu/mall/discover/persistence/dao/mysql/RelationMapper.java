package com.mall.discover.persistence.dao.mysql;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.discover.persistence.entity.article.RelationEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


@Repository
public interface RelationMapper extends BaseMapper<RelationEntity> {


    @Update("update discover_relation set is_deleted=1 where to_id=#{articleId} and biztype in(1,2)")
    void deleteArticleRelation(@Param("articleId") Long articleId);

    @Insert("insert into discover_relation(biztype,from_id,to_id,is_deleted,create_time,update_time,create_user_id) " +
            "values(#{biztype},#{fromId},#{toId},#{status},#{createTime},#{updateTime},#{createUser})")
    void save(RelationEntity relationEntity);

    @Update("update discover_relation set is_deleted=1 where biztype=#{bizType} and from_id=#{goodsId} and to_id=#{articleId}")
    void deleteProductArticle(@Param("bizType")Integer bizType,
                              @Param("articleId")String articleId,
                              @Param("goodsId")String goodsId);

    /**
     * 统计关联to_id数量
     * @param productId
     * @param code
     * @return
     */
    @Select("select count(to_id) from discover_relation as dr inner join discover_article as da on da.article_id = dr.to_id " +
            "where da.is_deleted=0 and dr.is_deleted=0 and from_id=#{productId} and biztype=#{biztype}")
    int selectProductRelateArticleCount(@Param("productId") Integer productId, @Param("biztype") Integer code);

    @Select("select count(id) from discover_relation where biztype=2 and from_id=#{subjectId} and is_deleted=0")
    int countSubjectArticle(@Param("subjectId")Long subjectId);

    @Update("update discover_relation set sort=#{sort} where id=#{relationId}")
    void editRelateArticleSort(@Param("relationId")Long relationId, @Param("sort")long sort);
}
