package com.mall.discover.web.support.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.PropertySourcedRequestMappingHandlerMapping;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.web.Swagger2Controller;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qiujingwang
 * @version 1.0
 * @date 2019/01/08 下午3:55
 * @Description: TODO
 */
@Configuration
@ConditionalOnProperty(name = "swagger2.enabled", havingValue = "true")
@EnableSwagger2
public class Swagger2Config extends InstantiationAwareBeanPostProcessorAdapter {

    @Value("${swagger2.title:swagger2.title没设置}")
    private String title;
    @Value("${swagger2.desc:swagger2.desc没设置}")
    private String desc;
    @Value("${swagger2.version:swagger2.version没设置}")
    private String version;
//    @Value("${swagger2.contact:swagger2.contact没设置}")
//    private String contact;
    @Value("${swagger2.url:swagger2.url没设置}")
    private String url;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .enable(enableRestApi)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mall.discover.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(desc)
                .termsOfServiceUrl(url)
                .contact(new Contact("TAPD", "https://www.tapd.cn/62061237/prong/iterations/card_view", null))
                .version(version)
                .build();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("swagger2ControllerMapping")) {
            ((PropertySourcedRequestMappingHandlerMapping) bean).setOrder(Ordered.LOWEST_PRECEDENCE - 1000);
        }else if(bean instanceof SecurityConfiguration){
            SecurityConfiguration securityConfiguration = (SecurityConfiguration)bean;
            securityConfiguration.setSwaggerExcludesUrlSet(getSwagger2SecurityExcludesUrls());
        }

        return bean;
    }

    private Set<String> getSwagger2SecurityExcludesUrls() {
        //排除
        return Arrays.stream(new String[]{"/swagger-resources", "/swagger-resources/*", Swagger2Controller.DEFAULT_URL, "/csrf", "/webjars/*", "/swagger-ui.html"}).collect(Collectors.toSet());
    }
}
