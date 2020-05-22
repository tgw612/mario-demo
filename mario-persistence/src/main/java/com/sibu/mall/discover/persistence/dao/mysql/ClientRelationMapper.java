package com.mall.discover.persistence.dao.mysql;

import com.mall.discover.persistence.entity.mysql.RelationsEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: huangzhong
 * @Date: 2019/10/30
 * @Description:
 */
@Repository
public interface ClientRelationMapper {

    /**
     * 获取from_id集合
     * @return
     */
    LinkedHashSet<Integer> queryFromIdPage(@Param("toId")Long toId,
                                 @Param("startRecord") int startRecord,
                                 @Param("recordSize") int recordSize);

    /**
     * 获取from_id数量
     */
    Integer queryFromIdCount(@Param("toId")Long toId);

    /**
     * 批量新增
     * @param relationsEntities
     */
    void batchAddRelation(@Param("list") List<RelationsEntity> relationsEntities, @Param("toId")Long toId);

    /**
     *  批量逻辑删除
     * @param articleId
     * @param removeUserIds
     */
    void batchRemoveByToId(@Param("toId") Long articleId,
                           @Param("list") List<Integer> removeUserIds,
                           @Param("relationType") Integer relationType);
}
