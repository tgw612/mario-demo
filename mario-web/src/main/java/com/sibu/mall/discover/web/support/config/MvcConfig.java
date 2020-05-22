package com.mall.discover.web.support.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.doubo.boot.autoimport.CustomImportAutoConfiguration;
import com.doubo.common.constants.CommonConstants;
import com.doubo.json.spring.FastJsonConfigBean;
import com.mall.common.enums.AppName;
import com.mall.discover.web.support.exception.WebExceptionResolver;
import com.mall.discover.web.support.interceptor.SerialNoInitInterceptor;
import com.mall.discover.web.support.mvc.CustomServletModelAttributeMethodProcessor;
import com.mall.discover.web.support.validator.CustomValidatorFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018-03-24
 * Description:
 */
@EnableConfigurationProperties({WebMvcProperties.class, ResourceProperties.class})
@CustomImportAutoConfiguration({
        ServletWebServerFactoryAutoConfiguration.class,
        EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
})
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer, ApplicationContextAware, ResourceLoaderAware {

    private ApplicationContext applicationContext;
    private ResourceLoader resourceLoader;

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return registry -> {
            registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
            registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"));
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
            registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
        };
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10485760);//10M
        resolver.setMaxInMemorySize(4096);
        resolver.setDefaultEncoding(CommonConstants.UTF8);
        return resolver;
    }

    @Bean
    public CustomValidatorFactoryBean localValidator() {
        CustomValidatorFactoryBean customValidator = new CustomValidatorFactoryBean();
        customValidator.setProviderClass(org.hibernate.validator.HibernateValidator.class);
        customValidator.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        return customValidator;
    }

    @Bean
    public FastJsonConfigBean getFastJsonConfig() {
        FastJsonConfigBean fastJsonConfig = new FastJsonConfigBean();
        fastJsonConfig.setEnableDefault(true);
        fastJsonConfig.setEnableJsonUtil(true);
        return fastJsonConfig;
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Override
    public Validator getValidator() {
        return localValidator();
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("json", MediaType.APPLICATION_JSON_UTF8);
        configurer.mediaType("html", MediaType.valueOf("text/html;charset=UTF-8"));
        configurer.mediaType("xml", MediaType.valueOf("application/xml;charset=UTF-8"));
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
        //扩展名至mimeType的映射,即 /user.json => application/json
        configurer.favorPathExtension(false);
        //用于开启 /userinfo/123?format=json 的支持
        configurer.favorParameter(true);
        configurer.parameterName("mediaType");
        configurer.ignoreAcceptHeader(true);
        configurer.useRegisteredExtensionsOnly(true);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);;
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
//        registry.addResourceHandler("/favicon.ico").addResourceLocations("/images/favicon.ico");
//        registry.addResourceHandler("/favicon.png").addResourceLocations("/images/favicon.png");
//        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926).resourceChain(true)
//                .addResolver(new GzipResourceResolver());
//        registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(31556926);
//        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926).resourceChain(true).addResolver(new GzipResourceResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        boolean addDefault = false;
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(addDefault, getConverters());
        converters.addAll(httpMessageConverters.getConverters());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        MappedInterceptor serialNoInitInterceptor = new MappedInterceptor(new String[]{"/**"}, new String[]{"/error/403", "/error/404", "/error/405"}, new SerialNoInitInterceptor(AppName.SIBU_MALL_DISCOVER));
        registry.addInterceptor(serialNoInitInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomServletModelAttributeMethodProcessor());
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        HandlerExceptionResolver exceptionResolver = new WebExceptionResolver(getJsonMsgConverter());
        resolvers.add(exceptionResolver);
    }

    /**
     * enable default servlet
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DefaultFormattingConversionService defaultFormattingConversionService = (DefaultFormattingConversionService) registry;
        defaultFormattingConversionService.setEmbeddedValueResolver(new EmbeddedValueResolver(((ConfigurableApplicationContext) this.applicationContext).getBeanFactory()));
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.viewResolver(getJspViewResolver());
    }

    private HttpMessageConverter<Object> getJsonMsgConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        fastConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON_UTF8,
                MediaType.valueOf("application/x-www-form-urlencoded; charset=UTF-8"),
                MediaType.valueOf("text/plain;charset=UTF-8"),
                MediaType.valueOf("text/html;charset=UTF-8")
        ));
        fastConverter.setFastJsonConfig(getFastJsonConfig());
        return fastConverter;
    }

    private HttpMessageConverter<String> getStrMsgConverter() {
        return new StringHttpMessageConverter(CommonConstants.UTF8_CHARSET);
    }

    private HttpMessageConverter<byte[]> getByteArrConverter() {
        return new ByteArrayHttpMessageConverter();
    }

    private List<HttpMessageConverter<?>> getConverters() {
        List<HttpMessageConverter<?>> myConverters = new ArrayList<>();
        myConverters.add(getJsonMsgConverter());
        myConverters.add(getStrMsgConverter());
        myConverters.add(getByteArrConverter());
        return myConverters;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /*private ViewResolver getJspViewResolver() {
        InternalResourceViewResolver jspViewResolver = new InternalResourceViewResolver();
        jspViewResolver.setPrefix("/WEB-INF/jsp/");
        jspViewResolver.setSuffix(".jsp");
        jspViewResolver.setOrder(1);
        jspViewResolver.setContentType("text/html;charset=UTF-8");
        return jspViewResolver;
    }*/

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
