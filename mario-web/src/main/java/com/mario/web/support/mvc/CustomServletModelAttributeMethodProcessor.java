package com.mario.web.support.mvc;

import com.doubo.common.model.request.common.CommonRequest;
import com.doubo.security.util.ThreadContext;
import com.mall.common.utils.SeqGenUtil;
import com.mario.web.support.filter.TokenUidSubject;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

/**
 * Created with IntelliJ IDEA. User: qiujingwang Date: 2016/4/28 Description:用于绑定moduleId的方法参数解析器(设置为当前登录会员的memberId)
 *
 * @see org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor
 */
public class CustomServletModelAttributeMethodProcessor extends
    ServletModelAttributeMethodProcessor {

  /**
   * @-param annotationNotRequired if "true", non-simple method arguments and return values are
   * considered model attributes with or without a {@code @ModelAttribute} annotation.
   */
  public CustomServletModelAttributeMethodProcessor(/*boolean annotationNotRequired*/) {
    super(true);
  }

  @Override
  protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
    //解决--限制了list最大只能增长到256
    binder.setAutoGrowCollectionLimit(2500);
    super.bindRequestParameters(binder, request);
    bindModuleId(binder);
  }

  /**
   * 绑定moduleId
   *
   * @param binder
   */
  private void bindModuleId(WebDataBinder binder) {
    if (binder.getTarget() instanceof CommonRequest) {//入参如果是标准入参
      CommonRequest commonRequest = (CommonRequest) binder.getTarget();
      String currentUserId = null;
      TokenUidSubject subject = (TokenUidSubject) ThreadContext.getSubject();
      if (subject != null && subject.isAuthenticated() == true) { //已登录的用户
        currentUserId = subject.getUid() != null ? subject.getUid().toString() : null;
      }
      // currentUserId只是提供给服务器端token转userId使用，客户端如果上传该字段直接忽略掉即可
      commonRequest.setCurrentUserId(currentUserId);
      //SerialNo.init();//设置流水号
      commonRequest.setInitiationID(SeqGenUtil.getLogId());
    }
  }
}
