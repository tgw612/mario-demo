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
import com.mall.discover.request.client.ClientSubjectArticlePageRequest;
import com.mall.discover.request.client.ClientSubjectInfoRequest;
import com.mall.discover.request.client.ClientSubjectPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientSubjectInfoResponse;
import com.mall.discover.response.client.ClientSubjectPageResponse;
import com.mall.discover.service.ClientSubjectService;
import com.mall.discover.web.common.constants.DubboConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: huangzhong
 * @Date: 2019/9/29
 * @Description:
 */
@Api(value = "话题模块", tags = {"话题模块"})
@RestController
@Slf4j
@RequestMapping(value = "/client/subject")
public class ClientSubjectController {

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientSubjectService clientSubjectService;

    /**
     *  话题列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.SUBJECT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientSubjectPageResponse>> subjectQueryPage;

    /**
     *  话题详情缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.SUBJECT_QUERY_INFO,
            cacheType = CacheType.REMOTE)
    private Cache<Long, ClientSubjectInfoResponse> subjectQueryInfo;

    /**
     *  话题内文章缓存,key:话题id-页码
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.SUBJECT_QUERY_ARTICLE_PAGE_OLD,
            cacheType = CacheType.REMOTE)
    private Cache<String, CommonPageResult<ClientArticleResponse>> subjectQueryArtilePageOld;


    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "话题列表分页", httpMethod = "GET", notes = "话题列表分页,只需要页码")
    @GetMapping("/querySubjectPage")
    public CommonResponse<CommonPageResult<ClientSubjectPageResponse>> querySubjectPage(@Valid ClientSubjectPageRequest request){
        CommonPageResult<ClientSubjectPageResponse> result = new CommonPageResult<>();
        try {
            result = subjectQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("老版本-获取缓存话题列表分页出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            return clientSubjectService.querySubjectPage(request, false);
        }
        return ResponseManage.success(result);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "话题详情", httpMethod = "GET", notes = "话题详情")
    @GetMapping("/querySubjectInfo")
    public CommonResponse<ClientSubjectInfoResponse> querySubjectInfo(@Valid ClientSubjectInfoRequest request){
        ClientSubjectInfoResponse infoResponse = new ClientSubjectInfoResponse();
        try {
            //自增阅读数
            RedisTemplateUtil.incrLong(DiscoverCountEnum.SUBJECT_READ_COUNT.getCode() + request.getSubjectId());
            //获取返回值
            infoResponse = subjectQueryInfo.get(request.getSubjectId());
        } catch (Exception e) {
            log.error("老版本-获取缓存话题详情出错,错误信息：{}", e.getMessage());
        }

        if (infoResponse == null) {
            CommonResponse<ClientSubjectInfoResponse> result = clientSubjectService.querySubjectInfo(request, false);
            if (!result.isSuccess()) {
                log.error("老版本-DB中获取话题详情出错,错误信息：{}", result);
                return ResponseManage.fail("该话题详情不存在");
            } else {
                return result;
            }
        }
        return ResponseManage.success(infoResponse);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "话题内文章列表", httpMethod = "GET", notes = "话题内文章列表")
    @GetMapping("/querySubjectArticlePage")
    public CommonResponse<CommonPageResult<ClientArticleResponse>> querySubjectArticlePage(@Valid ClientSubjectArticlePageRequest request){
        CommonPageResult<ClientArticleResponse> result = new CommonPageResult<>();
        try {
            result = subjectQueryArtilePageOld.get(request.getSubjectId() + "-" + request.getCurrentPage());
        } catch (Exception e) {
            log.error("老版本-获取缓存话题内文章列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            CommonResponse<CommonPageResult<ClientArticleResponse>> response = clientSubjectService.querySubjectArticlePageOld(request, false);
            if (!response.isSuccess()) {
                log.error("老版本-DB中获取话题详情内列表出错,错误信息：{}", response);
                return ResponseManage.success(new CommonPageResult<>());
            }
            result = response.getResult();
        }
        return ResponseManage.success(result);
    }

}
