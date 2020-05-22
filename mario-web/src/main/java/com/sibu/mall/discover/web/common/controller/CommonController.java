package com.mall.discover.web.common.controller;

import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.code.ErrorCodeEnum;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.web.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2016-4-9
 * Depiction:公用页面请求
 */
@Slf4j
@Controller
public class CommonController {

    static String ERR_UNAUTHORIZED_JSON = null;
    static String ERR_403_JSON = null;
    static String ERR_404_JSON = null;
    static String ERR_500_JSON = null;

    @GetMapping(value = "/login")
    public String login() {
        return "/login";
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "/index";
    }

    @RequestMapping(value = "/empty")
    public String empty() {
        return "/empty";
    }

    @GetMapping(value = "/error/403")
    @ResponseBody
    public void error403(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        /*if (HttpRequestUtil.isAjax(request)) {
            if (ERR_403_JSON == null) {
                ERR_403_JSON = JacksonUtil.toJson(ResponseRender.renderErr(ErrorCodeEnum.LIVE_ERR_403_ERROR));
            }
            HttpRequestUtil.writeJsonDate(response, ERR_403_JSON);
        } else {
            try {
                response.sendRedirect("/403.jsp");
            } catch (IOException e) {
                log.error("错误",e);
            }
        }*/
        if (ERR_403_JSON == null) {
            ERR_403_JSON = JsonUtil.toJson(ResponseManage.fail(ErrorCodeEnum.ERR_403_ERROR));
        }
        HttpUtil.writeJsonDate(response, ERR_403_JSON);
    }

    @GetMapping(value = "/error/404")
    @ResponseBody
    public void error404(HttpServletRequest request, HttpServletResponse response) {
        /*if (HttpRequestUtil.isAjax(request)) {
            if (ERR_404_JSON == null) {
                ERR_404_JSON = JacksonUtil.toJson(ResponseRender.renderErr(ErrorCodeEnum.LIVE_ERR_404_ERROR));
            }
            HttpRequestUtil.writeJsonDate(response, ERR_404_JSON);
        } else {
            try {
                response.sendRedirect("/404.jsp");
            } catch (IOException e) {
                log.error("错误",e);
            }
        }*/
        if (ERR_404_JSON == null) {
            ERR_404_JSON = JsonUtil.toJson(ResponseManage.fail(ErrorCodeEnum.ERR_404_ERROR));
        }
        HttpUtil.writeJsonDate(response, ERR_404_JSON);
    }

    @GetMapping(value = "/error/500")
    @ResponseBody
    public void error500(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        //log
        /*log.error("[{}]Error 500, err:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(ex));
        if (HttpRequestUtil.isAjax(request)) {
            if (ERR_500_JSON == null) {
                ERR_500_JSON = JacksonUtil.toJson(ResponseRender.renderErr(ErrorCodeEnum.LIVE_ERR_500_ERROR));
            }
            HttpRequestUtil.writeJsonDate(response, ERR_500_JSON);
        } else {
            try {
                response.sendRedirect("/500.jsp");
            } catch (IOException e) {
                log.error("错误",e);
            }
        }*/
        if (ERR_500_JSON == null) {
            ERR_500_JSON = JsonUtil.toJson(ResponseManage.fail(ErrorCodeEnum.ERR_500_ERROR));
        }
        HttpUtil.writeJsonDate(response, ERR_500_JSON);
    }
}
