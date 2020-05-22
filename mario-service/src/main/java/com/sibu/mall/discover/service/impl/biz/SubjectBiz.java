package com.mall.discover.service.impl.biz;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.BizTypeEnum;
import com.mall.discover.common.enums.RelationTypeEnum;
import com.mall.discover.common.util.BeanCopyUtils;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.config.properties.DiscoverProperties;
import com.mall.discover.persistence.bo.*;
import com.mall.discover.persistence.dao.mysql.ArticleMapper;
import com.mall.discover.persistence.dao.mysql.DiscoverSubjectMapper;
import com.mall.discover.persistence.entity.mysql.DiscoverCountEntity;
import com.mall.discover.persistence.entity.mysql.DiscoverSubjectEntity;
import com.mall.discover.persistence.vo.SubjectVo;
import com.mall.discover.request.subject.*;
import com.mall.discover.response.article.ArticleRelateProduct;
import com.mall.discover.response.article.SubjectInfoResponse;
import com.mall.discover.response.subject.ArticleHistorySubjectResponse;
import com.mall.discover.response.subject.SubjectArticleResponse;
import com.mall.discover.util.DiscoverUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Slf4j
@Service
public class SubjectBiz {

    @Autowired
    private DiscoverSubjectMapper subjectMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiscoverProperties discoverProperties;
    @Autowired
    ArticleServiceBiz articleServiceBiz;

    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.DISCOVER_ARTICLE_HISTORY_SUBJECT,
            cacheType = CacheType.REMOTE)
    private Cache<Long, LinkedHashSet<ArticleHistorySubjectResponse.HistorySubject>> ArticleIdHistorySubjectCache;


    @Transactional(rollbackFor = Exception.class)
    public Boolean subjectSubmit(SubjectSubmitRequestDto request) {

        //复制话题数据
        DiscoverSubjectEntity entity = new DiscoverSubjectEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setCreateUserId(request.getCurrentUserId());
        long createTime = System.currentTimeMillis();
        entity.setUpdateTime(createTime);

        //转换话题图片、音频等汇总信息
        try {
            entity.setSubjectLookString(objectMapper.writeValueAsString(request.getSubjectLook()));
        } catch (Exception e) {
            log.info("转换话题图片、音频等汇总信息出错,{}", e.getMessage());
            return false;
        }

        //复制统计数据
        DiscoverCountEntity countEntity = new DiscoverCountEntity();
        Long sort = request.getSort();
        countEntity.setSort(sort == null || sort <= 0 || sort > discoverProperties.getMaxSort() ? discoverProperties.getMaxSort() : sort);
        countEntity.setBizType(BizTypeEnum.SUBJECT.getCode());
        countEntity.setUpdateTime(createTime);

        if (request.getSubjectId() != null && request.getSubjectId() > 0) {
            //更新话题
            subjectMapper.updateSubject(entity);
            //更新统计
            countEntity.setBizId(request.getSubjectId());
            subjectMapper.updateSubjectCount(countEntity);
        } else {
            //新增话题
            entity.setCreateTime(createTime);
            subjectMapper.insertSubject(entity);
            //新增统计
            countEntity.setBizId(entity.getSubjectId());
            countEntity.setCreateTime(createTime);
            subjectMapper.insertSubjectCount(countEntity);
        }

        return true;
    }


    public CommonPageResult<SubjectBO> querySubjectPage(SubjectPageRequestDto request) {
        SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
        subjectVo.setSubjectName(request.getSubjectName());

        int count = subjectMapper.querySubjectPageCount(subjectVo);
        CommonPageResult<SubjectBO> result = new CommonPageResult<>();

        if (count > 0) {
            List<SubjectBO> subjectList = subjectMapper.querySubjectPage(subjectVo, PageUtils.getStartPage(request), request.getPageSize());

            //获取文章数量
            List<Long> subjectIdList = subjectList.parallelStream().map(SubjectBO::getSubjectId).collect(Collectors.toList());
            List<SubjectBO> articleCountList = subjectMapper.queryArticleCount(subjectVo, subjectIdList);
            subjectList.forEach(subject -> {
                int articleCount = articleCountList.stream().filter(a -> subject.getSubjectId().equals(a.getSubjectId()))
                        .findFirst()
                        .orElseGet(SubjectBO::new)
                        .getArticleCount();
                subject.setArticleCount(articleCount);
            });

            result.setTotalCount(count);
            result.setData(subjectList);
            result.setTotalPage(PageUtils.getPageCount(request.getPageSize(), count));
        }

        return result;
    }

    public SubjectBO querySubjectInfo(SubjectInfoRequestDto request) {
        SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
        subjectVo.setSubjectId(request.getSubjectId());

        SubjectBO subject = subjectMapper.querySubjectInfo(subjectVo);
        //获取文章数量
        List<SubjectBO> subjectList = subjectMapper.queryArticleCount(subjectVo, Collections.singletonList(request.getSubjectId()));
        if (subjectList.size() > 0) {
            subject.setArticleCount(subjectList.get(0).getArticleCount());
        }
        return subject;
    }


    public CommonPageResult<SubjectArticleResponse> querySubjectArticlePage(SubjectArticlePageRequestDto request) {
        SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
        subjectVo.setSubjectId(request.getSubjectId());
        subjectVo.setArticleId(request.getArticleId());
        //获取总条数
        int count = subjectMapper.querySubjectArticlePageCount(subjectVo);
        CommonPageResult<SubjectArticleResponse> result = new CommonPageResult<>();

        if (count > 0) {
            List<SubjectArticleBO> subjectArticles = subjectMapper.querySubjectArticlePage(subjectVo,
                    PageUtils.getStartPage(request), request.getPageSize());
            //暂时只关联一个话题
//            subjectVo.setBizType(BizTypeEnum.SUBJECT.getCode());
//            SubjectBO subjectVO = subjectMapper.querySubjectInfo(subjectVo);

            //转换为返回对象
            List<SubjectArticleResponse> responseList = new ArrayList<>();
            subjectArticles.forEach(subject -> {
                try {
                    String value = objectMapper.writeValueAsString(subject);
                    SubjectArticleResponse response = objectMapper.readValue(value, SubjectArticleResponse.class);
                    //设置关联商品
                    List<ProductIdNameBO> productIdNameBOList = articleMapper.getArticleRelateProduct(subject.getArticleId(),
                            RelationTypeEnum.GOODS_ARTICLE.getCode());
                    List<ArticleRelateProduct> products = BeanCopyUtils.copyList(ArticleRelateProduct.class, productIdNameBOList);
                    response.setProduct(products);
                    //图片数量
                    String articleLookStr = subject.getArticleLook();
                    if (StringUtils.isNotBlank(articleLookStr)) {
                        ArticleLook articleLook = objectMapper.readValue(articleLookStr, ArticleLook.class);
                        if (articleLook == null || articleLook.getPictureLookList() == null) {
                            response.setPictureNums(0);
                        } else {
                            response.setArticleLooks(articleLook);
                            response.setPictureNums(articleLook.getPictureLookList().size());
                        }
                    }
                    //设置relate表中sort值,如果为空则为默认
                    if (subject.getSort() == null) {
                        response.setSort(discoverProperties.getMaxSort());
                    }
                    //设置点赞数
                    response.setLikeCount(articleServiceBiz.getArticleLikeCount(subject.getArticleId()));
                    //设置话题
                    List<SubjectInfoBO> relateSubjectName = articleMapper.getRelateSubjectName(subject.getArticleId(),
                            RelationTypeEnum.SUBJECT_ARTICLE.getCode());
                    List<SubjectInfoResponse> subjectNameList = BeanCopyUtils
                            .copyList(SubjectInfoResponse.class, relateSubjectName);
                    //话题名称集合
                    response.setSubject(subjectNameList);
                    //
                    responseList.add(response);
                } catch (Exception e) {
                    log.info("转换话题内文章出错,{}", e.getMessage());
                }
            });

            //分页封装
            result.setTotalCount(count);
            result.setData(responseList);
            result.setTotalPage(PageUtils.getPageCount(request.getPageSize(), count));
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSubject(SubjectDeleteRequestDto request) {
        SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
        subjectVo.setSubjectId(request.getSubjectId());
        //获取文章数量
        List<SubjectBO> subjectList = subjectMapper.queryArticleCount(subjectVo, Collections.singletonList(request.getSubjectId()));
        //有关联文章，无法删除
        if (subjectList != null && subjectList.size() > 0) {
            return false;
        }

        //删除话题
        subjectMapper.deleteSubject(subjectVo);
        //删除话题统计信息
        subjectMapper.deleteSubjectCount(subjectVo);
        return true;
    }


    public Boolean deleteSubjectArticle(SubjectArticleDeleteRequestDto request) {
        SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
        subjectVo.setSubjectId(request.getSubjectId());
        subjectVo.setArticleId(request.getArticleId());

        //删除话题文章关联关系
        subjectMapper.deleteSubjectArticle(subjectVo);
        return true;
    }


    public CommonPageResult<SubjectBO> querySubjectByName(SubjectNameRequestDto request) {
        //查询数量上限
        long maxQueryNumber = discoverProperties.getQuerySubjectByName();
        SubjectVo subjectVo = new SubjectVo();
        subjectVo.setSubjectName(request.getSubjectName());
        List<SubjectBO> subjectList = subjectMapper.querySubjectByName(subjectVo, maxQueryNumber);

        CommonPageResult<SubjectBO> result = new CommonPageResult<>();
        //分页封装
        result.setTotalCount(subjectList.size());
        result.setData(subjectList);
        result.setTotalPage(1);
        return result;
    }

    public CommonResponse<ArticleHistorySubjectResponse> articleHistorySubject() {
        List<HistorySubjectBO> data = subjectMapper.queryHistorySubject();
        List<ArticleHistorySubjectResponse.HistorySubject> list = BeanCopyUtils.copyList(ArticleHistorySubjectResponse.HistorySubject.class, data);
        ArticleHistorySubjectResponse response = new ArticleHistorySubjectResponse();
        response.setHistorySubjectList(list);
        return ResponseManage.success(response);
    }
}
