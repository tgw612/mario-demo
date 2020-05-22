package com.mall.discover.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.request.PrimaryIdRequest;
import com.doubo.common.model.request.PrimarySeqRequest;
import com.doubo.common.model.request.common.CommonRequest;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.util.ConverterUtil;
import com.doubo.common.util.StringUtil;
import com.doubo.json.util.JsonUtil;
import com.mall.commission.service.DisSaleSellerProductService;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.code.ErrorCodeEnum;
import com.mall.common.manage.ResponseManage;
import com.mall.common.utils.BeanCopyUtils;
import com.mall.common.utils.SeqGenUtil;
import com.mall.discover.request.api.FindShareCountRequest;
import com.mall.discover.request.api.ListCateRequest;
import com.mall.discover.request.api.MemberPageQueryDiscoverListRequest;
import com.mall.discover.request.api.SubmitDiscoverRequest;
import com.mall.discover.response.api.MemberDiscoverCateListResponse;
import com.mall.discover.response.api.MemberPageQueryDiscoverListResponse;
import com.mall.discover.service.MemberDiscoverService;
import com.mall.discover.web.common.constants.DubboConstants;
import com.mall.discover.web.request.DiscoverPageQueryRequest;
import com.mall.discover.web.request.DiscoverSubmitRequest;
import com.mall.discover.web.response.MemberDiscoverResponse;
import com.mall.discover.web.util.ServletUtils;
import com.mall.discover.web.vo.DiscoverPictureVo;
import com.mall.discover.web.vo.UserInfoVo;
import com.mall.product.service.ProductService;
import com.mall.search.request.ProductSearchRequest;
import com.mall.search.request.SearchProductUserAttrRequest;
import com.mall.search.response.SearchProductBriefResponse;
import com.mall.search.service.ESProductSearchService;
import com.mall.user.request.FindUserInfoRequest;
import com.mall.user.response.MemberResponse;
import com.mall.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;


@Service
@Slf4j
public class MallDiscoverService {

    @Value("${mall.system.wechat.h5.base.url}")
    private String mallSystemWechatH5BaseUrl;

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private MemberDiscoverService memberDiscoverService;

    @Reference(consumer = DubboConstants.SIBU_MALL_PRODUCT_CONSUMER)
    private ProductService productService;

    @Reference(consumer = DubboConstants.SIBU_MALL_USER_CONSUMER)
    private UserService userService;

    @Reference(consumer = DubboConstants.SIBU_MALL_COMMISSION_CONSUMER)
    private DisSaleSellerProductService disSaleSellerProductService;

    @Reference(consumer = DubboConstants.SIBU_MALL_ELASTICSEARCH_CONSUMER)
    private ESProductSearchService esProductSearchService;

//    public CommonResponse<CommonPageResult<MemberDiscoverResponse>> pageDiscover(DiscoverPageQueryRequest discoverPageQueryRequest){
//        MemberPageQueryDiscoverListRequest memberPageQueryDiscoverListRequest = new MemberPageQueryDiscoverListRequest();
//        memberPageQueryDiscoverListRequest.setCateId(discoverPageQueryRequest.getCateId());
//        memberPageQueryDiscoverListRequest.setProductId(discoverPageQueryRequest.getProductId());
//        memberPageQueryDiscoverListRequest.setCurrentPage(discoverPageQueryRequest.getCurrentPage());
//        memberPageQueryDiscoverListRequest.setPageSize(discoverPageQueryRequest.getPageSize());
//        //
//        memberPageQueryDiscoverListRequest.setInitiationID(discoverPageQueryRequest.getInitiationID());
//
//        CommonPageResult<MemberDiscoverResponse> pageResult = null;
//
//        CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> pageResultCommonResponse = memberDiscoverService.pageDiscover(memberPageQueryDiscoverListRequest);
//        if(pageResultCommonResponse.isSuccess()){
//            CommonPageResult<MemberPageQueryDiscoverListResponse> memberPageQueryDiscoverListResponsePageResult = pageResultCommonResponse.getResult();
//            List<MemberDiscoverResponse> datas = new ArrayList<>();
//            for (MemberPageQueryDiscoverListResponse each : memberPageQueryDiscoverListResponsePageResult.getData()) {
//                MemberDiscoverResponse response = BeanCopyUtils.copyProperties(MemberDiscoverResponse.class,each);
//
//                if(StringUtil.isNotBlank(each.getPictures())){
//                    response.setPictureVoList(JsonUtil.parseArray(each.getPictures(),DiscoverPictureVo.class));
//                    FindListByIdRequest findListByIdRequest = new FindListByIdRequest();
//                    findListByIdRequest.setInitiationID(discoverPageQueryRequest.getInitiationID());
//                    findListByIdRequest.setProductIds(response.getPictureVoList().stream().map(DiscoverPictureVo::getProductId).collect(Collectors.toList()));
//                    CommonResponse<List<ClientProductResponse>> listById = productService.findListById(findListByIdRequest);
//                    List<ClientProductResponse> result = new ArrayList<>();
//                    if(listById.isSuccess()){
//                        result = listById.getResult();
//                    }else {
//                        log.error("调用商品微服务productService.findListById()出错:" + listById.getErrorMsg());
//                    }
//                    List<ClientProductResponse> finalResult = result;
//                    response.getPictureVoList().stream().forEach(o->{
//                        if(!StringUtil.isNull(o.getProductId())){
//                            //通过发现保存图片分享时，图片上二维码需要含分享人shareMemberId=id
//                            o.setProductUrl(mallSystemWechatH5BaseUrl + "/#/goodsDetail?id=" + o.getProductId()+"&shareMemberId={}&referrerId={}");
//                            ClientProductResponse productResponse = finalResult.stream().filter(p -> p.getId().intValue() == o.getProductId()).findFirst().orElse(null);
//                            if(productResponse != null){
//                                o.setProductName(productResponse.getName1());
//                                o.setProductMasterImg(productResponse.getMasterImg());
//                                o.setMalMobilePrice(productResponse.getMalMobilePrice());
//                                o.setMarketPrice(productResponse.getMarketPrice());
//                                //取商品佣金
//                                GetCommissionProductConfigRequest getCommissionProductConfigRequest=new GetCommissionProductConfigRequest();
//                                getCommissionProductConfigRequest.setProductCateId(productResponse.getProductCateId());
//                                getCommissionProductConfigRequest.setProductId(productResponse.getId());
//                                getCommissionProductConfigRequest.setInitiationID(SeqGenUtil.getLogId());
//                                CommonResponse<GetProductCateCommissionResponse> productCommissionPercentResponse = disSaleSellerProductService.getProductCommissionPercent(getCommissionProductConfigRequest);
//                                if(productCommissionPercentResponse.isSuccess()){
//                                    GetProductCateCommissionResponse getProductCateCommissionResponse = productCommissionPercentResponse.getResult();
//                                    if(!StringUtil.isNull(getProductCateCommissionResponse.getProductCommissionPercent())){
//                                        BigDecimal productCommissionPercent = getProductCateCommissionResponse.getProductCommissionPercent();
//                                        BigDecimal multiply = productCommissionPercent.multiply(o.getMalMobilePrice()).setScale(2, BigDecimal.ROUND_DOWN);
//                                        o.setCommisson(multiply);
//                                    }
//                                }
//                            }
//                        }
//                    });
//                }
//
//                if(StringUtil.isNotBlank(each.getVideo()) && !StringUtil.isNull(each.getProductId())){
//                    response.setProductId(each.getProductId());
//                    //通过发现保存图片分享时，图片上二维码需要含分享人shareMemberId=id
//                    response.setProductUrl(mallSystemWechatH5BaseUrl + "/#/goodsDetail?id=" + each.getProductId()+"&shareMemberId={}&referrerId={}");
//                    FindProductRequest findProductRequest = new FindProductRequest();
//                    findProductRequest.setInitiationID(discoverPageQueryRequest.getInitiationID());
//                    findProductRequest.setProductId(each.getProductId());
//                    CommonResponse<ClientProductResponse> productResponseCommonResponse = productService.get(findProductRequest);
//                    if(productResponseCommonResponse.isSuccess()){
//                        response.setProductName(productResponseCommonResponse.getResult().getName1());
//                        response.setProductMasterImg(productResponseCommonResponse.getResult().getMasterImg());
//                        response.setMalMobilePrice(productResponseCommonResponse.getResult().getMalMobilePrice());
//                        response.setMarketPrice(productResponseCommonResponse.getResult().getMarketPrice());
//                        //取商品佣金
//                        GetCommissionProductConfigRequest getCommissionProductConfigRequest=new GetCommissionProductConfigRequest();
//                        getCommissionProductConfigRequest.setProductCateId(productResponseCommonResponse.getResult().getProductCateId());
//                        getCommissionProductConfigRequest.setProductId(productResponseCommonResponse.getResult().getId());
//                        getCommissionProductConfigRequest.setInitiationID(SeqGenUtil.getLogId());
//                        CommonResponse<GetProductCateCommissionResponse> productCommissionPercentResponse = disSaleSellerProductService.getProductCommissionPercent(getCommissionProductConfigRequest);
//                        if(productCommissionPercentResponse.isSuccess()){
//                            GetProductCateCommissionResponse getProductCateCommissionResponse = productCommissionPercentResponse.getResult();
//                            if(!StringUtil.isNull(getProductCateCommissionResponse.getProductCommissionPercent())){
//                                BigDecimal productCommissionPercent = getProductCateCommissionResponse.getProductCommissionPercent();
//                                BigDecimal multiply = productCommissionPercent.multiply(response.getMalMobilePrice()).setScale(2, BigDecimal.ROUND_DOWN);
//                                response.setCommisson(multiply);
//                            }
//                        }
//                    }else {
//                        log.error("调用商品微服务productService.get()出错:" + productResponseCommonResponse.getErrorMsg());
//                    }
//                }
//                datas.add(response);
//            }
//
//            pageResult = ConverterUtil.convertToPageResult(memberPageQueryDiscoverListResponsePageResult, datas);
//        }
//        return ResponseManage.success(pageResult);
//    }


    public CommonResponse<CommonPageResult<MemberDiscoverResponse>> pageDiscover(DiscoverPageQueryRequest discoverPageQueryRequest) {
        MemberPageQueryDiscoverListRequest memberPageQueryDiscoverListRequest = new MemberPageQueryDiscoverListRequest();
        memberPageQueryDiscoverListRequest.setCateId(discoverPageQueryRequest.getCateId());
        memberPageQueryDiscoverListRequest.setProductId(discoverPageQueryRequest.getProductId());
        memberPageQueryDiscoverListRequest.setCurrentPage(discoverPageQueryRequest.getCurrentPage());
        memberPageQueryDiscoverListRequest.setPageSize(discoverPageQueryRequest.getPageSize());
        //
        memberPageQueryDiscoverListRequest.setInitiationID(discoverPageQueryRequest.getInitiationID());
        CommonPageResult<MemberDiscoverResponse> pageResult = null;
        CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> pageResultCommonResponse = memberDiscoverService.pageDiscover(memberPageQueryDiscoverListRequest);
        if (pageResultCommonResponse.isSuccess()) {
            CommonPageResult<MemberPageQueryDiscoverListResponse> memberPageQueryDiscoverListResponsePageResult = pageResultCommonResponse.getResult();
            List<MemberDiscoverResponse> datas = new ArrayList<>();
            Set<Integer> productIds = new HashSet<>(memberPageQueryDiscoverListResponsePageResult.getData().size());
            List<MemberDiscoverResponse> responseList = new ArrayList<>(memberPageQueryDiscoverListResponsePageResult.getData().size());
            for (MemberPageQueryDiscoverListResponse each : memberPageQueryDiscoverListResponsePageResult.getData()) {
                MemberDiscoverResponse response = BeanCopyUtils.copyProperties(MemberDiscoverResponse.class, each);
                if (StringUtil.isNotBlank(each.getPictures())) {
                    response.setPictureVoList(JsonUtil.parseArray(each.getPictures(), DiscoverPictureVo.class));
                    Integer paroductId=null;
                    String paroductName=null;
                    String paroductUrl=null;
                    for (DiscoverPictureVo item : response.getPictureVoList()) {
                        if (item.getProductId() != null){
                            paroductId=item.getProductId();
                            paroductName=item.getProductName();
                            paroductUrl=item.getProductUrl();
                            productIds.add(item.getProductId());
                        }else {
                            item.setProductName(paroductName);
                            item.setProductId(paroductId);
                            item.setProductUrl(paroductUrl);
                        }
                    }
                }
                if (StringUtil.isNotBlank(each.getVideo()) && !StringUtil.isNull(each.getProductId())) {
                    response.setProductId(each.getProductId());
                    productIds.add(each.getProductId());
                }
                responseList.add(response);
            }
            SearchProductUserAttrRequest userAttr = null;
            if (StringUtils.isNotBlank(discoverPageQueryRequest.getCurrentUserId()) && StringUtils.isNumeric(discoverPageQueryRequest.getCurrentUserId())) {
                userAttr = new SearchProductUserAttrRequest(Integer.parseInt(discoverPageQueryRequest.getCurrentUserId()));
            }
            List<SearchProductBriefResponse> searchProductBriefResponseList = null;
            if (productIds != null && productIds.size() > 0) {
                List<Integer> productIdList = new ArrayList<>(productIds);
                searchProductBriefResponseList = this.findProductsByIds(productIdList, discoverPageQueryRequest.getInitiationID(), userAttr);
            }
            Map<Integer, SearchProductBriefResponse> productBriefResponseMap = null;
            if (searchProductBriefResponseList != null && searchProductBriefResponseList.size() > 0) {
                productBriefResponseMap = new HashMap<>(searchProductBriefResponseList.size());
                for (SearchProductBriefResponse itemResponse : searchProductBriefResponseList) {
                    productBriefResponseMap.put(itemResponse.getId(), itemResponse);
                }
            }
            for (MemberDiscoverResponse response : responseList) {
                if (response.getPictureVoList() != null && response.getPictureVoList().size() > 0) {
                    for (DiscoverPictureVo itemPictVo : response.getPictureVoList()) {
                        if (!StringUtil.isNull(itemPictVo.getProductId())) {
                            SearchProductBriefResponse searchProductBriefResponse = productBriefResponseMap != null ? productBriefResponseMap.get(itemPictVo.getProductId()) : null;
                            //通过发现保存图片分享时，图片上二维码需要含分享人shareMemberId=id
                            itemPictVo.setProductUrl(mallSystemWechatH5BaseUrl + "/#/goodsDetail?id=" + itemPictVo.getProductId() + "&shareMemberId={}&referrerId={}");
                            if (searchProductBriefResponse != null) {
                                this.fillDiscoverPictureVo(itemPictVo, searchProductBriefResponse);
                            }
                        }
                    }
                }
                if (response.getProductId() != null) {
                    SearchProductBriefResponse searchProductBriefResponse = productBriefResponseMap != null ? productBriefResponseMap.get(response.getProductId()) : null;
                    //通过发现保存图片分享时，图片上二维码需要含分享人shareMemberId=id
                    response.setProductUrl(mallSystemWechatH5BaseUrl + "/#/goodsDetail?id=" + response.getProductId() + "&shareMemberId={}&referrerId={}");
                    if (searchProductBriefResponse != null) {
                        this.fillMemberDiscoverResponse(response, searchProductBriefResponse);
                    }
                }
                datas.add(response);
            }
            pageResult = ConverterUtil.convertToPageResult(memberPageQueryDiscoverListResponsePageResult, datas);
        }
        return ResponseManage.success(pageResult);
    }

    public List<SearchProductBriefResponse> findProductsByIds(@NotNull List<Integer> productIdList, @Nullable String sequenceId, @Nullable SearchProductUserAttrRequest userAttr) {
        ProductSearchRequest searchRequest = new ProductSearchRequest();
        searchRequest.setInitiationID(sequenceId == null ? SeqGenUtil.getLogId() : sequenceId);
        searchRequest.setProductIds(productIdList);
        searchRequest.setUserAttr(userAttr);
        searchRequest.setProductClaszz(SearchProductBriefResponse.class);
        CommonResponse<List<SearchProductBriefResponse>> listCommonResponse = esProductSearchService.searchByRequest(searchRequest);
        return (listCommonResponse != null && listCommonResponse.getResult() != null && listCommonResponse.getResult().size() > 0) ? listCommonResponse.getResult() : null;
    }

    public CommonResponse<UserInfoVo> checkUser(CommonRequest request) {
        FindUserInfoRequest findUserInfoRequest = new FindUserInfoRequest();
        findUserInfoRequest.setToken(ServletUtils.getToken());
        findUserInfoRequest.setInitiationID(request.getInitiationID());
        CommonResponse<MemberResponse> userInfo = userService.findUserInfo(findUserInfoRequest);
        if (userInfo.isSuccess()) {
            UserInfoVo userInfoResponse = BeanCopyUtils.copyProperties(UserInfoVo.class, userInfo.getResult());
            return ResponseManage.successIfNotNull(userInfoResponse);
        }
        return new CommonResponse<>(ErrorCodeEnum.BEAN_BIZ_ERROR.getErrorCode(), "获取用户信息失败");
    }

    public CommonResponse<Boolean> submitDiscover(DiscoverSubmitRequest request, UserInfoVo userInfoVo) {
        SubmitDiscoverRequest submitDiscoverRequest = BeanCopyUtils.copyProperties(SubmitDiscoverRequest.class, request);
        submitDiscoverRequest.setMemberId(userInfoVo.getId());
        submitDiscoverRequest.setMemberLogo(userInfoVo.getHeadImg());
        submitDiscoverRequest.setMemberName(userInfoVo.getNickName());
        submitDiscoverRequest.setMemberPhone(userInfoVo.getPhone());
        submitDiscoverRequest.setInitiationID(request.getInitiationID());
        //小视频
        if (StringUtil.isNotBlank(request.getVideo())) {
            submitDiscoverRequest.setSameProductId(submitDiscoverRequest.getProductId());
            submitDiscoverRequest.setSameProductFlag(true);
            submitDiscoverRequest.setPictures("");
            submitDiscoverRequest.setVideoHeight(!StringUtil.isEmpty(request.getVideoHeight()) ? Integer.parseInt(request.getVideoHeight()) : null);
            submitDiscoverRequest.setVideoWidth(!StringUtil.isEmpty(request.getVideoWidth()) ? Integer.parseInt(request.getVideoWidth()) : null);
        } else if (CollectionUtils.isNotEmpty(request.getPictureVoList())) {
            if (request.getPictureVoList().size() > 9) {
                return ResponseManage.fail("图片数量不能超过9张");
            }
            Long distinctCount = request.getPictureVoList().stream().map(o -> o.getProductId()).distinct().count();
            if (distinctCount <= 1) {
                submitDiscoverRequest.setSameProductFlag(true);
                submitDiscoverRequest.setSameProductId(request.getPictureVoList().get(0).getProductId());
            }
            submitDiscoverRequest.setVideo(null);
            submitDiscoverRequest.setVideoHeight(null);
            submitDiscoverRequest.setVideoWidth(null);
            submitDiscoverRequest.setProductId(null);
            submitDiscoverRequest.setPictures(JsonUtil.toJson(request.getPictureVoList()));
        } else {
            return ResponseManage.fail("请上传图片或者上传小视频");
        }

        return memberDiscoverService.submitDiscover(submitDiscoverRequest);
    }

    public CommonResponse<List<MemberDiscoverCateListResponse>> listCate(CommonRequest commonRequest) {
        ListCateRequest listCateRequest = new ListCateRequest();
        listCateRequest.setInitiationID(commonRequest.getInitiationID());
        return memberDiscoverService.listCate(listCateRequest);
    }

    public CommonResponse<Boolean> forward(PrimarySeqRequest idRequest) {
        return memberDiscoverService.forward(idRequest);
    }

    public CommonResponse<Integer> getProductShareCount(PrimaryIdRequest primaryIdRequest) {
        return memberDiscoverService.getProductShareCount(primaryIdRequest);
    }

    public CommonResponse<Integer> findShareCount(String productId) {
        FindShareCountRequest request = new FindShareCountRequest();
        request.setInitiationID(SeqGenUtil.getLogId());
        request.setProductId(productId);
        CommonResponse<Integer> commonResponse = memberDiscoverService.findShareCount(request);
        if (!commonResponse.isSuccess()) {
            log.error("调用[memberDiscoverService][findShareCount]dubbo 接口出错,request:{},response:{}",
                    JSON.toJSONString(request), JSON.toJSONString(commonResponse));
            throw new BusinessException(commonResponse.getErrorMsg());
        }
        return commonResponse;
    }

    private void fillDiscoverPictureVo(DiscoverPictureVo itemPictVo, SearchProductBriefResponse product) {
        itemPictVo.setMalMobilePrice(product.getPrice());
        itemPictVo.setMarketPrice(product.getMarketPrice());
        itemPictVo.setCommisson(product.getCommission());
        itemPictVo.setProductName(product.getName());
        itemPictVo.setProductMasterImg(product.getMasterImg());
        if (product.getActives() != null && product.getActives().size() > 0) {
            itemPictVo.setActives(product.getActives());
        }
    }

    private void fillMemberDiscoverResponse(MemberDiscoverResponse response, SearchProductBriefResponse product) {
        response.setProductName(product.getName());
        response.setProductMasterImg(product.getMasterImg());
        response.setMalMobilePrice(product.getPrice());
        response.setMarketPrice(product.getMarketPrice());
        response.setCommisson(product.getCommission());
        if (product.getActives() != null && product.getActives().size() > 0) {
            response.setActives(product.getActives());
        }
    }
}
