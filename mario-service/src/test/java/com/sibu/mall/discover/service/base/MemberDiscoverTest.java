package com.mall.discover.service.base;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.request.PrimarySeqRequest;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.api.MemberPageQueryDiscoverListRequest;
import com.mall.discover.request.api.SubmitCateRequest;
import com.mall.discover.request.api.SubmitDiscoverRequest;
import com.mall.discover.response.api.MemberPageQueryDiscoverListResponse;
import com.mall.discover.service.impl.biz.MemberDiscoverBiz;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MemberDiscoverTest extends BaseSpringTest {

    @Autowired
    private MemberDiscoverBiz memberDiscoverBiz;

    @Autowired
    private MongoTemplate template;

    @Test
    public void submitDiscoverTest(){
        SubmitDiscoverRequest submitDiscoverRequest = new SubmitDiscoverRequest();
        submitDiscoverRequest.setMemberId(213);
        submitDiscoverRequest.setMemberPhone("15920927312");
        submitDiscoverRequest.setMemberName("王");
        submitDiscoverRequest.setCateId("234");
        submitDiscoverRequest.setCateName("每日爆款");
        submitDiscoverRequest.setPictures("3525");
        submitDiscoverRequest.setContent("你好");
        memberDiscoverBiz.submitDiscover(submitDiscoverRequest);
     }

     @Test
     public void pageDiscoverTest(){
         MemberPageQueryDiscoverListRequest memberPageQueryDiscoverListRequest = new MemberPageQueryDiscoverListRequest();
         memberPageQueryDiscoverListRequest.setCurrentPage(1);
         memberPageQueryDiscoverListRequest.setPageSize(10);
         CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> pageResultCommonResponse = memberDiscoverBiz.pageDiscover(memberPageQueryDiscoverListRequest);
     }

     @Test
     public void submitCateTest(){
         SubmitCateRequest request = new SubmitCateRequest();
         request.setCateName("每日爆款");
         request.setOrder(1);
         memberDiscoverBiz.submitCate(request);
         request.setCateName("营销素材");
         request.setOrder(2);
         memberDiscoverBiz.submitCate(request);
         request.setCateName("高佣商品");
         request.setOrder(3);
         memberDiscoverBiz.submitCate(request);
         request.setCateName("心灵鸡汤");
         request.setOrder(4);

         memberDiscoverBiz.submitCate(request);
     }

     @Test
     public void updateViewsTest(){
         PrimarySeqRequest discoverIdRequest = new PrimarySeqRequest();
         discoverIdRequest.setId("5c3efa7563c33d01944e78b6");
         memberDiscoverBiz.views(discoverIdRequest);
     }

     @Test
    public void findShareCountTest(){
         CommonResponse<Integer> shareCount = memberDiscoverBiz.findShareCount("43620");
         System.out.println(shareCount);
     }
}
