//package com.mall.discover.service.impl;
//
//import com.doubo.common.model.request.common.CommonRequest;
//import com.mall.discover.request.admin.AdminDiscoverListCateRequest;
//import com.mall.discover.request.admin.AdminPageDiscoverListRequest;
//import com.mall.discover.service.AdminDiscoverService;
//import com.mall.discover.service.base.BaseSpringTest;
//import com.mall.discover.service.impl.biz.AdminDiscoverBiz;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//public class AdminDiscoverServiceImplTest extends BaseSpringTest {
//
//    @Autowired
//    private AdminDiscoverService adminDiscoverService;
//
//    @Autowired
//    private AdminDiscoverBiz adminDiscoverBiz;
//
//    @Test
//    public void testPageDiscover() throws Exception {
//
//        System.out.println("test start");
//
//        AdminPageDiscoverListRequest request = new AdminPageDiscoverListRequest();
//        request.setCurrentPage(1);
//        request.setPageSize(10);
//        request.setCurrentUserId("1");
//        request.setInitiationID("1");
//        //System.out.println(adminDiscoverBiz.pageDiscover(request));
//
//        System.out.println(adminDiscoverService.listPage(request));
//        System.out.println("test end");
//    }
//
//    @Test
//    public void testCateList() throws Exception {
//        AdminDiscoverListCateRequest request = new AdminDiscoverListCateRequest();
////        request.setCurrentPage(1);
////        request.setPageSize(10);
//        request.setCurrentUserId("1");
//        request.setInitiationID("1");
//        System.out.println(adminDiscoverService.listCate(request));
//    }
//
//    @Test
//    public void testCateListPage() throws Exception {
//        AdminDiscoverListCateRequest request = new AdminDiscoverListCateRequest();
//        request.setCurrentPage(1);
//        request.setPageSize(10);
//        request.setCurrentUserId("1");
//        request.setInitiationID("1");
//        System.out.println(adminDiscoverService.listCatePage(request));
//    }
//
//}