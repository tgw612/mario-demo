package com.mall.discover.common.util;

import com.doubo.common.model.request.common.CommonRequest;
import com.mall.common.utils.SeqGenUtil;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018-03-08
 * Description:初始化请求类的日志ID
 */
public class LogIdUtil {
    /**
     * 生成日志ID
     * @return
     */
    public static String create(){
        return SeqGenUtil.getLogId();
    }

    /**
     * 请求类日志ID为空时，则设置请求类的日志ID
     * @return
     */
    public static void setIfNull(CommonRequest request){
        if(request.getInitiationID() == null){
            request.setInitiationID(create());
        }
    }

    /**
     * srcReq日志ID为空时，则设置srcReq的日志ID，并将其初始化为targetReq的日志ID
     * @return
     */
    public static void setTargetFromSrc(CommonRequest srcReq, CommonRequest targetReq){
        if(srcReq.getInitiationID() == null){
            srcReq.setInitiationID(create());
        }
        targetReq.setInitiationID(srcReq.getInitiationID());
    }

    public static void main(String args[]){
        System.out.println(create());
        System.out.println(create());
        System.out.println(create());
        System.out.println(create());
    }
}
