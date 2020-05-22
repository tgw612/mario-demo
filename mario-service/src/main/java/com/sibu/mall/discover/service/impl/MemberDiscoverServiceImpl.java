package com.mall.discover.service.impl;

import com.alicp.jetcache.CacheGetResult;
import com.mall.common.manage.ResponseManage;
import org.apache.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.request.PrimaryIdRequest;
import com.doubo.common.model.request.PrimarySeqRequest;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.util.StringUtil;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.request.admin.AdminPageQueryDiscoverListRequest;
import com.mall.discover.request.admin.AdminUpdateDiscoverRequest;
import com.mall.discover.request.api.*;
import com.mall.discover.response.api.MemberDiscoverCateListResponse;
import com.mall.discover.response.api.MemberDiscoverDetailResponse;
import com.mall.discover.response.api.MemberPageQueryDiscoverListResponse;
import com.mall.discover.service.MemberDiscoverService;
import com.mall.discover.service.impl.biz.MemberDiscoverBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service(
        group = "${dubbo.provider.group}",
        version = "${dubbo.provider.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class MemberDiscoverServiceImpl  implements MemberDiscoverService {

    @Autowired
    private MemberDiscoverBiz memberDiscoverBiz;

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

    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.INDEX_DISCOVER__SHARE_PRODUCT_PRE,
            cacheType = CacheType.REMOTE)
    private Cache<String, String> discoverShareProductCache;
    @Override
    public CommonResponse<Boolean> submitDiscover(SubmitDiscoverRequest request) {
        return memberDiscoverBiz.submitDiscover(request);
    }

    @Override
    public CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> pageDiscover(MemberPageQueryDiscoverListRequest request) {
        return memberDiscoverBiz.pageDiscover(request);
    }

    @Override
    public CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> adminPageDiscover(AdminPageQueryDiscoverListRequest request) {
        return memberDiscoverBiz.adminPageDiscover(request);
    }

    @Override
    public CommonResponse<MemberDiscoverDetailResponse> detail(PrimarySeqRequest request) {
        MemberDiscoverDetailResponse detailResponse = JSON.parseObject(discoverCache.get(request.getId() + ""), MemberDiscoverDetailResponse.class);
        if(StringUtil.isNull(detailResponse)){
            detailResponse = memberDiscoverBiz.getDetail(request).getResult();
            discoverCache.put(request.getId(), JSON.toJSONString(detailResponse),5,TimeUnit.MINUTES);
        }
        return new CommonResponse<>(detailResponse);
    }

    @Override
    public CommonResponse<Integer> getProductShareCount(PrimaryIdRequest productIdRequest) {
        String s = discoverProductCache.get(productIdRequest.getId().toString());
        if (StringUtil.isNull(s)) {
            CommonResponse<Integer> productShareCount = memberDiscoverBiz.getProductShareCount(productIdRequest);
            discoverProductCache.put(productIdRequest.getId().toString(),productShareCount.getResult().toString(),2,TimeUnit.MINUTES);
            return productShareCount;
        }
        return new CommonResponse(Integer.parseInt(s));
    }

    @Override
    public CommonResponse<Boolean> forward(PrimarySeqRequest request) {
        return memberDiscoverBiz.forward(request);
    }

    @Override
    public CommonResponse<Boolean> views(PrimarySeqRequest request) {
        return memberDiscoverBiz.views(request);
    }

    @Override
    public CommonResponse<Boolean> updateByPrimaryKeySelective(AdminUpdateDiscoverRequest request) {
        return memberDiscoverBiz.updateByPrimaryKeySelective(request);
    }

    @Override
    public CommonResponse<Boolean> submitCate(SubmitCateRequest request) {
        return memberDiscoverBiz.submitCate(request);
    }

    @Override
    public CommonResponse<List<MemberDiscoverCateListResponse>> listCate(ListCateRequest request) {
        return memberDiscoverBiz.listCate(request);
    }

    @Override
    public CommonResponse<Integer> findShareCount(FindShareCountRequest request) {
        String s = discoverShareProductCache.get(request.getProductId());
        if (StringUtil.isNull(s)) {
            CommonResponse<Integer> findShareCount2 = memberDiscoverBiz.findShareCount(request.getProductId());
            discoverShareProductCache.put(request.getProductId(),findShareCount2.getResult().toString(),5,TimeUnit.MINUTES);
            return findShareCount2;
        }
        return ResponseManage.success(Integer.parseInt(s));
        //return memberDiscoverBiz.findShareCount(request.getProductId());
    }

}
