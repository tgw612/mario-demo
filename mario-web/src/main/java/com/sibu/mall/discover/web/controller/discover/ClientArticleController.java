package com.mall.discover.web.controller.discover;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.redis.util.RedisTemplateUtil;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.DiscoverCountEnum;
import com.mall.discover.request.client.ClientArticleInfoRequest;
import com.mall.discover.request.client.ClientArticlePageRequest;
import com.mall.discover.request.client.ClientUpdateCountRequest;
import com.mall.discover.response.client.ClientArticleInfoResponse;
import com.mall.discover.response.client.ClientDiscoverResponse;
import com.mall.discover.service.ClientArticleService;
import com.mall.discover.web.common.constants.DubboConstants;
import com.mall.discover.web.service.impl.biz.ClientArticleBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: huangzhong
 * @Date: 2019/9/29
 * @Description:
 */
@Api(value = "文章模块", tags = {"文章模块"})
@RestController
@Slf4j
@RequestMapping(value = "/client/article")
public class ClientArticleController {

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientArticleService clientArticleService;

    @Autowired
    private ClientArticleBiz clientArticleBiz;

    /**
     *  文章列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.ARTICLE_QUERY_PAGE_OLD,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientDiscoverResponse>> articleQueryPageOld;

    /**
     *  文章详情缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.ARTICLE_QUERY_INFO_OLD,
            cacheType = CacheType.REMOTE)
    private Cache<Long, ClientArticleInfoResponse> articleQueryInfoOld;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "文章列表分页", httpMethod = "GET", notes = "文章列表分页,只需要页码")
    @GetMapping("/queryArticlePage")
    public CommonResponse<CommonPageResult<ClientDiscoverResponse>> queryArticlePage(@Valid ClientArticlePageRequest request){
        CommonPageResult<ClientDiscoverResponse> result = new CommonPageResult<>();
        try {
            result = articleQueryPageOld.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("老版本-获取缓存文章列表出错,错误信息：{}", e.getMessage());
        }

        //DB中获取
        if (result == null) {
            CommonResponse<CommonPageResult<ClientDiscoverResponse>> response = clientArticleService.queryArticlePageOld(request, false);
            if (!response.isSuccess()) {
                log.error("老版本-DB中获取文章列表出错,错误信息：{}", response);
                return ResponseManage.success(new CommonPageResult<>());
            }
            result = response.getResult();
        }
        return ResponseManage.success(result);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "文章详情", httpMethod = "GET", notes = "文章详情")
    @GetMapping(value = "/queryArticleInfo")
    public CommonResponse<ClientArticleInfoResponse> articleInfo(@Valid ClientArticleInfoRequest request) {
        ClientArticleInfoResponse infoResponse = new ClientArticleInfoResponse();
        try {
            //自增阅读数
            RedisTemplateUtil.incrLong(DiscoverCountEnum.ARTICLE_READ_COUNT.getCode() + request.getArticleId());
            //获取返回值
            infoResponse = articleQueryInfoOld.get(request.getArticleId());
        } catch (Exception e) {
            log.error("老版本-获取缓存文章详情出错,错误信息：{}", e.getMessage());
        }

        //DB中获取
        if (infoResponse == null) {
            CommonResponse<ClientArticleInfoResponse> result = clientArticleService.queryArticleInfoOld(request, false);
            if (!result.isSuccess()) {
                log.error("老版本-DB中获取文章详情出错,错误信息：{}", result);
                return ResponseManage.fail("该文章详情不存在");
            } else {
                infoResponse = result.getResult();
            }
        }
        return ResponseManage.success(infoResponse);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "更新分享数",httpMethod = "POST",  notes = "更新分享数")
    @PostMapping(value = "/updateCount")
    public CommonResponse<Boolean> updateCount(@Valid ClientUpdateCountRequest request) {
       return clientArticleBiz.updateCount(request);
    }


}
