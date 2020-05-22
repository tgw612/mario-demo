package com.mall.discover.web.support.exception;

import com.doubo.common.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018-03-28
 * Description:
 */
@Slf4j
public class WebExceptionResolver extends AbstractHandlerMethodExceptionResolver {

    private HttpMessageConverter<Object> messageConverter;

    private WebExceptionHandler webExceHandler = new WebExceptionHandler();

    public WebExceptionResolver() {
    }

    public WebExceptionResolver(HttpMessageConverter<Object> messageConverter) {
        this.messageConverter = messageConverter;
    }

    public WebExceptionResolver(HttpMessageConverter<Object> messageConverter, WebExceptionHandler webExceHandler) {
        this.messageConverter = messageConverter;
        this.webExceHandler = webExceHandler;
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
                                                           @Nullable HandlerMethod handlerMethod, Exception ex) {
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
        response.setStatus(200);
        Object outputValue = getResultWithException(request, response, handlerMethod, ex);
        try {
            messageConverter.write(outputValue, MediaType.APPLICATION_JSON_UTF8, outputMessage);
        } catch (Throwable e) {
            // Any other than the original exception is unintended here,
            // probably an accident (e.g. failed assertion or the like).
            log.error("Failed to invoke HandlerMethodException: {}, outputValue:{}, WebExceptionResolverException:{}, SystemException:{}", handlerMethod, outputValue, ExceptionUtil.getAsString(e), ExceptionUtil.getAsString(ex));
            // Continue with default processing of the original exception...
            return null;
        }

        return new ModelAndView();
    }

    private Object getResultWithException(HttpServletRequest request, HttpServletResponse response,
                                          @Nullable HandlerMethod handlerMethod, Exception ex) {
        return webExceHandler.forException(request, response, handlerMethod, ex);
    }

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        return new ServletServerHttpResponse(response);
    }

    protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        return new ServletServerHttpRequest(servletRequest);
    }
}
