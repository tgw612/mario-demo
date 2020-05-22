package com.mall.discover.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.request.admin.*;
import com.mall.discover.response.admin.AdminDiscoverCateFindCateByIdResponse;
import com.mall.discover.response.admin.AdminDiscoverDetailResponse;
import com.mall.discover.response.admin.AdminDiscoverListCateResponse;
import com.mall.discover.response.admin.AdminPageDiscoverListResponse;
import com.mall.discover.service.AdminDiscoverService;
import com.mall.discover.service.impl.biz.AdminDiscoverBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Service(
        group = "${dubbo.provider.group}",
        version = "${dubbo.provider.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class AdminDiscoverServiceImpl implements AdminDiscoverService {

    @Autowired
    private AdminDiscoverBiz adminDiscoverBiz;

    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.INDEX_COMMEND_DISCOVER_DETAIL_PRE,
            cacheType = CacheType.REMOTE)
    private Cache<String, String> discoverCache;

    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.INDEX_DISCOVER_PRODUCT_PRE,
            cacheType = CacheType.REMOTE)
    private Cache<String, String> discoverProductCache;


    @Override
    public CommonResponse<CommonPageResult<AdminPageDiscoverListResponse>> listPage(AdminPageDiscoverListRequest request) {
        return adminDiscoverBiz.listPage(request);
    }

    @Override
    public CommonResponse<Boolean> add(AdminDiscoverAddRequest request) {
        return adminDiscoverBiz.add(request);
    }

    @Override
    public CommonResponse<Boolean> update(AdminDiscoverAddRequest request) {
        return adminDiscoverBiz.update(request);
    }

    @Override
    public CommonResponse<Integer> delete(AdminDiscoverDetailRequest request) {
        return adminDiscoverBiz.delete(request);
    }

    @Override
    public CommonResponse<AdminDiscoverDetailResponse> detail(AdminDiscoverDetailRequest request) {
        return adminDiscoverBiz.detail(request);
    }


    @Override
    public CommonResponse<List<AdminDiscoverListCateResponse>> listCate(AdminDiscoverListCateRequest request) {
        return adminDiscoverBiz.listCate(request);
    }

    @Override
    public CommonResponse<CommonPageResult<AdminDiscoverListCateResponse>> listCatePage(AdminDiscoverListCateRequest request) {
        return adminDiscoverBiz.listCatePage(request);
    }

    @Override
    public CommonResponse<Boolean> addCate(AdminDiscoverCateAddCateRequest request) {
       return adminDiscoverBiz.addCate(request);
    }

    @Override
    public CommonResponse<Integer> deleteCateById(AdminDiscoverCateFindCateByIdRequest request) {
        return adminDiscoverBiz.deleteCateById(request);
    }

    @Override
    public CommonResponse<Boolean> updateCateById(AdminDiscoverCateAddCateRequest request) {
        return adminDiscoverBiz.updateCateById(request);
    }

    @Override
    public CommonResponse<AdminDiscoverCateFindCateByIdResponse> findCateById(AdminDiscoverCateFindCateByIdRequest request) {
        return adminDiscoverBiz.findCateById(request);
    }

}
