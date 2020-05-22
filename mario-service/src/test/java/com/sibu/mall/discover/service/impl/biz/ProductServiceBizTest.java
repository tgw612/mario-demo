package com.mall.discover.service.impl.biz;

import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.service.base.BaseSpringTest;
import com.mall.search.request.ProductSearchRequest;
import com.mall.search.request.QueryPageRequest;
import com.mall.search.response.PageResponse;
import com.mall.search.response.SearchProductResponse;
import com.mall.search.service.ESProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ProductServiceBizTest extends BaseSpringTest {

    @Reference(consumer = "elasticsearch-consumer-config")
    private ESProductSearchService esProductSearchService;

    @Test
    public void testSearchProductPage() {
        QueryPageRequest pageRequest = new QueryPageRequest();
        pageRequest.setPageSize(10);
        pageRequest.setCurrentPage(1);
        List<String> keywordList = new ArrayList<>();
//        keywordList.add("ceshi555");
//        keywordList.add("测试");
        keywordList.add("手机");
        ProductSearchRequest request = new ProductSearchRequest();
        request.setKeywordsList(keywordList);
        request.setProductClaszz(SearchProductResponse.class);
        request.setPageRequest(pageRequest);

//        request.setCategoryId(10321);
//
//        SortRequest sortRequest = new SortRequest();
//        sortRequest.setSortBy(2);
//        sortRequest.setDirection(-1);
//
//        request.setSort(sortRequest);
        CommonResponse<PageResponse<SearchProductResponse>> pageResponse = esProductSearchService.searchByPage(request);
        log.info("响应数据：{}", pageResponse);
        if (pageResponse != null && pageResponse.getResult() != null && pageResponse.getResult().getDatas() != null && pageResponse.getResult().getDatas().size() > 0) {
            for (SearchProductResponse itemSearchProductResponse : pageResponse.getResult().getDatas()) {
                log.info("{}", itemSearchProductResponse);
                log.info("\n");
                log.info("\n");
            }
        }
    }

    @Autowired
    ProductServiceBiz productServiceBiz;

    @Test
    public void scheduleUpdateProductEntityV2() {
        productServiceBiz.scheduleUpdateProductEntityV2();
    }

}
