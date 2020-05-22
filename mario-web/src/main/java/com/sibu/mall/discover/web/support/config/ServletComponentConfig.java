package com.mall.discover.web.support.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qiujingwang
 * @version 1.0
 * @date 2019/02/15 下午4:39
 * @Description: Servlet Component Config
 */
@Configuration
@ServletComponentScan
public class ServletComponentConfig {

    /**
     * 健康检查
     */
    @WebServlet(name = "healthCheckServlet", urlPatterns = "/health_check")
    public static class HealthCheckServlet extends HttpServlet {
        @Override
        protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            doGet(req, resp);
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.getWriter().append("health_check");
        }

    }
}
