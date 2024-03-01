package com.dailystudy.swinglab.service.framework.config;

import com.dailystudy.swinglab.service.framework.http.response.PlatformResponseBuilder;
import com.dailystudy.swinglab.service.framework.core.annotation.JacksonConvertor;
import com.dailystudy.swinglab.service.framework.utils.ResourceScanner;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TimeZone;

/**
 * 항상 scan항목에 포함되도록 하여 줍니다.
 */
@Slf4j
@Configuration
public class ApiCommonBeans
{
    @Bean
    public PlatformResponseBuilder macrogenResponseBuilder ()
    {
        return new PlatformResponseBuilder();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public MappingJackson2HttpMessageConverter jsonConverter ()
    {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        SimpleModule simpleModule = new SimpleModule();

        //         Set<BeanDefinition> beanDefinitions =
        //             ResourceScanner.scanByAssignableFilter(CommonConst.JACKSON_HANDLER_PKG, Object.class);
        Set<BeanDefinition> beanDefinitions = ResourceScanner.scan("com.dailystudy", JacksonConvertor.class);
        for (BeanDefinition bd : beanDefinitions)
        {
            String beanClassName = bd.getBeanClassName();
            try
            {
                Class<?> clazz = Class.forName(beanClassName);
                if (JsonDeserializer.class.isAssignableFrom(clazz))
                {
                    JsonDeserializer o = (JsonDeserializer) clazz.getConstructor().newInstance();
                    simpleModule.addDeserializer(o.handledType(), o);
                } else if (JsonSerializer.class.isAssignableFrom(clazz))
                {
                    JsonSerializer o = (JsonSerializer) clazz.getConstructor().newInstance();
                    simpleModule.addSerializer(o);
                }
            } catch (ClassNotFoundException |
                     InstantiationException |
                     IllegalAccessException |
                     NoSuchMethodException |
                     InvocationTargetException ex)
            {
                log.error(ex.getMessage(), ex);
            }
        }

        ObjectMapper mapper = new ObjectMapper().registerModule(simpleModule);
        // mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // mapper.getSerializerProvider().setNullValueSerializer(new NullToEmpryStringSerializer());
        mapper.setTimeZone(TimeZone.getDefault());
        converter.setObjectMapper(mapper);
        return converter;
    }
}
