package com.dailystudy.swinglab.service.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSpringDataWebSupport
public class PlatformWebConfig extends DelegatingWebMvcConfiguration
{
    final Integer cachePeriod = 3600 * 30;
//    private final List<String> defResourceLocations = new ArrayList<>(Arrays.asList("/favicon.ico", "/css/", "/js/", "/images/", "/fonts/"));

    @Autowired
    private MappingJackson2HttpMessageConverter jsonConvertor;

    @Autowired
    @Qualifier(value = "platformHttpInterceptor")
    private HandlerInterceptor interceptor;

    @Override
    protected void configureContentNegotiation (ContentNegotiationConfigurer configurer)
    {
        super.configureContentNegotiation(configurer);
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    protected void configureMessageConverters (List<HttpMessageConverter<?>> converters)
    {
        addDefaultHttpMessageConverters(converters);
        converters.add(jsonConvertor);
    }

    @Override
    // @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping (
            @Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
            @Qualifier("mvcConversionService") FormattingConversionService conversionService,
            @Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider)
    {

        RequestMappingHandlerMapping handler = super.requestMappingHandlerMapping(contentNegotiationManager, conversionService, resourceUrlProvider);
        handler.setOrder(1);
        return handler;
    }

    @Override
    public void configureViewResolvers (ViewResolverRegistry registry)
    {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);

        registry.viewResolver(resolver);
    }

    private void addResourceInRegistry (ResourceHandlerRegistry registry, String resourceLocation)
    {
        String pathPattern = resourceLocation;
        if (resourceLocation.endsWith("/"))
        {
            pathPattern = resourceLocation.concat("**");
        }

        registry.addResourceHandler(pathPattern).addResourceLocations(resourceLocation).setCachePeriod(cachePeriod)
                .resourceChain(false);
    }

    @Override
    public void addInterceptors (InterceptorRegistry registry)
    {
        registry.addInterceptor(interceptor).addPathPatterns("/**")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/file/**", "/fileDownload/**", "/")
                .excludePathPatterns("/fonts/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/js/**");
    }

}
