package com.mario.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.doubo.common.enums.AppName;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.threadlocal.SerialNo;
import com.doubo.trace.dubbo.trace.AbstractTraceFilter;
import com.mario.web.response.MemberDiscoverResponse;
import com.mario.web.service.base.BaseSpringTest;
import org.apache.log4j.MDC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liivin
 * @title: MallDiscoverServiceTest
 * @projectName mario
 * @description:
 * @date 2019/7/30
 */
public class MallDiscoverServiceTest extends BaseSpringTest {

  @Autowired
  MallDiscoverService mallDiscoverService;

  @Test
  public void testPageDiscover() throws Exception {

    String initiationID = SerialNo.init(AppName.DOUBO_SC);//设置流水号
    String initIdKeyName = AbstractTraceFilter.INITIATION_ID_NAME;
            /*String initiationID = SerialNo.getSerialNo();
            if(StringUtil.isBlank(initiationID)){
                initiationID = SeqGenUtil.getLogId();
            }*/
    MDC.put(initIdKeyName, initiationID);
    SerialNo.setSerialNo(initiationID);

    DiscoverPageQueryRequest request = new DiscoverPageQueryRequest();
    request.setCateId("122444");
    request.setProductId("121134");
    request.setCurrentPage(1);
    request.setPageSize(10);
    request.setInitiationID("AAAAAAAAAAAAAAAAAAAA");
    CommonResponse<CommonPageResult<MemberDiscoverResponse>> discover = mallDiscoverService
        .pageDiscover(request);
    /*CommonResponse<CommonPageResult<MemberDiscoverResponse>> discover2 = mallDiscoverService.pageDiscover(request);*/
    System.out.println(JSON.toJSONString(discover));
  }
}