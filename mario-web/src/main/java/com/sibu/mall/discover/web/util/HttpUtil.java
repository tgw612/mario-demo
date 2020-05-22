package com.mall.discover.web.util;

import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.threadlocal.SerialNo;
import com.doubo.common.util.ExceptionUtil;
import com.doubo.common.util.StringUtil;
import com.doubo.json.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class HttpUtil {

    private final static String webStaticFile = ".css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.htm,.html,.crx,.xpi,.exe,.ipa,.apk";
    private final static String urlSuffix = ".html";

    // 静态文件后缀
    private final static String[] staticFiles = StringUtil.split(webStaticFile, ",");

    /**
     * 输出json格式提示 并且带对象消息
     *
     * @param response
     * @param result   提示消息
     */
    public static void writeDate(ServletResponse response, CommonResponse result) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JsonUtil.toJson(result));
        } catch (Exception e) {
            log.error("[{}] [{}] Finish handling .\nSome Exception Occur:[{}]", SerialNo.getSerialNo(), HttpUtil.class.getName(), ExceptionUtil.getAsString(e));
        }
    }

    /**
     * 输出json格式提示 并且带对象消息
     *
     * @param response
     * @param json     提示消息
     */
    public static void writeJsonDate(ServletResponse response, String json) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error("[{}] [{}] Finish handling .\nSome Exception Occur:[{}]", SerialNo.getSerialNo(), HttpUtil.class.getName(), ExceptionUtil.getAsString(e));
        }
    }

    /**
     * 是否为ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
//		Principal principal = UserUtils.getPrincipal();

        // 如果是异步请求或是手机端，则直接返回信息
        return ((accept != null && accept.indexOf("application/json") != -1
                || (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
            /*|| (principal != null && principal.isMobileLogin())*/));
    }

    /**
     * 是否为请求json数据
     */
    public static boolean isRequestJson(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return (contentType != null && contentType.indexOf("application/json") != -1);
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * <p>
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Validate.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(pre) || paramName.startsWith(pre)) {
                String unprefixed = paramName.substring(pre.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    values = new String[]{};
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
     */
    public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
        StringBuilder queryStringBuilder = new StringBuilder();

        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append("&");
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * 获取当前请求对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断访问URI是否是静态文件请求
     *
     * @throws Exception
     */
    public static boolean isStaticFile(String uri) {
        if (staticFiles == null) {
            try {
                throw new Exception("检测到“app.properties”中没有配置“web.staticFile”属性。配置示例：\n#静态文件后缀\n"
                        + "web.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
            } catch (Exception e) {
                log.error("错误",e);
            }
        }
//		if ((StringUtils.startsWith(uri, "/static/") || StringUtils.endsWithAny(uri, sfs))
//				&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
//			return true;
//		}
        if (StringUtil.endsWithAny(uri, staticFiles) && !StringUtil.endsWithAny(uri, urlSuffix)
                && !StringUtil.endsWithAny(uri, ".jsp") && !StringUtil.endsWithAny(uri, ".java")) {
            return true;
        }
        return false;
    }
}
