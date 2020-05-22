package com.mall.discover.task.jobhandler.article;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.BusinessException;
import com.mall.discover.common.article.DiscoverCountDTO;
import com.mall.discover.common.enums.BizTypeEnum;
import com.mall.discover.request.article.EditArticleSortClientRequest;
import com.mall.discover.request.article.ListDiscoverCountRequest;
import com.mall.discover.response.article.DiscoverCountListResponse;
import com.mall.discover.service.ArticleService;
import com.mall.discover.task.common.constants.DubboConstants;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章话题商品阅读数/分享数更新
 */
@JobHandler(value = "articleSortJobHandler")
@Service
@Slf4j
public class ArticleSortJobHandler extends IJobHandler {

    private static final int PAGE_SIZE = 1024;
    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    ArticleService articleService;

    //RedisTemplate<String, Long> redisTemplate;
    //redisTemplate.opsForZSet().add(ARTICLE_SORT_REDIS_KEY, obj.getArticleId(), x);
    private static final String ARTICLE_SORT_REDIS_KEY = "discover.article.sort";


    /**
     * 发帖时间距离当前时间小时数量 = T
     * （59 分为 1 小时，1 小时 1 分为 2 小时。
     * 阅读数 = R
     * 分享数 = S
     * X = S+0.1R-10T
     * X 越大，排序越靠前。
     * 一小时更新一次。
     *
     * @return
     */
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        ListDiscoverCountRequest request = new ListDiscoverCountRequest();
        request.setPageSize(1000);
        request.setBiztype(BizTypeEnum.ARTICLE.getCode());
        CommonResponse<CommonPageResult<DiscoverCountListResponse>> response = articleService.listDiscoverCount(request);
        //可将排序值存入
        for (int i = 1; i <= response.getResult().getTotalPage(); i++) {
            request.setCurrentPage(i);
            response = articleService.listDiscoverCount(request);
            List<DiscoverCountListResponse> data = response.getResult().getData();
            List<DiscoverCountDTO> infos = new ArrayList<>(request.getPageSize());
            data.forEach(obj -> {
                DiscoverCountDTO dto =new DiscoverCountDTO();
                dto.setId(obj.getId());
                dto.setSortClient((long)caculateClientSort(obj));
                infos.add(dto);
            });
            EditArticleSortClientRequest sortClientRequest = new EditArticleSortClientRequest();
            sortClientRequest.setBachList(infos);
            CommonResponse<Boolean> editArticleSortClientResponse = articleService.editArticleSortClient(sortClientRequest);
            if (!editArticleSortClientResponse.isSuccess()) {
                XxlJobLogger.log("调用[articleService][editArticleSortClient]dubbo 接口出错,request:{},response:{}",
                        JsonUtil.toJson(request), JsonUtil.toJson(response));
                throw new BusinessException(response.getErrorMsg());
            }
        }
        return SUCCESS;
    }

    private int caculateHour(Long createTime) {
        Long currentTime = System.currentTimeMillis();
        return (int) ((currentTime - createTime) / (1000 * 3600));
    }

    private double caculateClientSort(DiscoverCountListResponse obj) {
        int t = caculateHour(obj.getCreateTime());
        double x = obj.getShareCount() + 0.1 * obj.getReadCount() - 10 * t;
        return x;
    }
}
