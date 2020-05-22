package com.mall.discover.persistence.dao.mysql;

import com.mall.discover.persistence.bo.HistorySubjectBO;
import com.mall.discover.persistence.bo.SubjectArticleBO;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.persistence.entity.mysql.DiscoverCountEntity;
import com.mall.discover.persistence.entity.mysql.DiscoverSubjectEntity;
import com.mall.discover.persistence.vo.SubjectVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Repository
public interface DiscoverSubjectMapper {

    /**
     * 新增话题
     * @param entity
     */
    void insertSubject(DiscoverSubjectEntity entity);

    /**
     * 更新话题
     * @param entity
     */
    void updateSubject(DiscoverSubjectEntity entity);

    /**
     * 更新话题排序
     * @param entity
     */
    void updateSubjectCount(DiscoverCountEntity entity);

    /**
     * 新增话题排序
     * @param entity
     */
    void insertSubjectCount(DiscoverCountEntity entity);

    /**
     * 获取话题列表count
     */
    int querySubjectPageCount(@Param("param") SubjectVo subjectVo);

    /**
     * 获取话题列表
     */
    List<SubjectBO> querySubjectPage(@Param("param") SubjectVo subjectVo,
                                     @Param("startRecord") int startRecord,
                                     @Param("recordSize") int recordSize);

    /**
     * 获取话题列表文章数量
     */
    List<SubjectBO> queryArticleCount(@Param("param") SubjectVo subjectVo,
                                      @Param("list") List<Long> subjectIdList );

    /**
     * 获取话题详情
     */
    SubjectBO querySubjectInfo(@Param("param") SubjectVo subjectVo);

    /**
     * 删除话题
     */
    void deleteSubject(@Param("param") SubjectVo subjectVo);

    /**
     * 删除话题统计信息
     */
    void deleteSubjectCount(@Param("param")SubjectVo subjectVo);

    /**
     * 删除话题文章关联关系
     */
    void deleteSubjectArticle(@Param("param")SubjectVo subjectVo);

    /**
     *  通过名称模糊搜索话题
     */
    List<SubjectBO> querySubjectByName(@Param("param")SubjectVo subjectVo, @Param("maxQueryNumber") long maxQueryNumber);

    /**
     * 获取话题内文章列表
     */
    List<SubjectArticleBO> querySubjectArticlePage(@Param("param") SubjectVo subjectVo,
                                                   @Param("startRecord") int startRecord,
                                                   @Param("recordSize") int recordSize);

    /**
     * 获取话题内文章数量
     */
    int querySubjectArticlePageCount(@Param("param")SubjectVo subjectVo);

    /**
     * 获取需更改发布状态的话题id集合
     * @param currentTimeMillis
     * @return
     */
    List<Long> queryWaitPublishSubjectId(@Param("currentTime") long currentTimeMillis, @Param("subjectStatus") Integer subjectStatus);

    /**
     * 更改话题的发布状态
     */
    void updateWaitPublishSubStatus(@Param("currentTime")long currentTimeMillis,
                                    @Param("subjectStatus")Integer subjectStatus,
                                    @Param("list")List<Long> subjectIds);


    @Select("select publish_time from discover_subject where subject_id=#{id}")
    Long queryPublishTime(@Param("id") Long subjectId);

    @Select("select subject_name from discover_subject where subject_id=#{id}")
    String querySubjectNameById(@Param("id") Long subjectId);

    @Select("select subject_id,subject_name,publish_time from discover_subject order by update_time desc limit 0, 10")
    List<HistorySubjectBO> queryHistorySubject();

}
