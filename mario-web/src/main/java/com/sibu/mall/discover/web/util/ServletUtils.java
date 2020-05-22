package com.mall.discover.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * Author: 陈二伟
 * Date:2018/11/22
 */
public abstract class ServletUtils {

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getHeadValueByName(String headName){
        return getRequest().getHeader(headName);
    }

    public static String getToken(){
        return getRequest().getHeader("token");
    }
}
