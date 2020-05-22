package com.mall.discover.persistence.dao.mysql;

import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.persistence.bo.SubjectArticleBO;
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
public interface ClientSubjectMapper {
    /**
     * 获取话题列表
     */
    List<SubjectBO> querySubjectPage(@Param("param") SubjectVo subjectVo,
                                     @Param("startRecord") int startRecord,
                                     @Param("recordSize") int recordSize);

    /**
     * 获取话题列表count
     */
    Integer querySubjectPageCount(@Param("param") SubjectVo subjectVo);

    /**
     * 批量获取话题列表文章数量
     */
    List<SubjectBO> queryArticleCount(@Param("param") SubjectVo subjectVo,
                                      @Param("list") List<Long> subjectIdList );

    /**
     * 获取话题详情
     */
    SubjectBO querySubjectInfo(@Param("param") SubjectVo subjectVo);

    /**
     * 获取话题内文章列表
     */
    List<SubjectArticleBO> querySubjectArticlePage(@Param("param") SubjectVo subjectVo,
                                                   @Param("startRecord") int startRecord,
                                                   @Param("recordSize") int recordSize);

    /**
     * 获取话题内文章列表count
     */
    Integer querySubjectArticlePageCount(@Param("param") SubjectVo subjectVo);

    List<Long> queryAllSubjectId();
}
