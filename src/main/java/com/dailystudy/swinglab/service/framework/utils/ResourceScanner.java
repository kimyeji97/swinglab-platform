package com.dailystudy.swinglab.service.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Resource 스캔 기능을 제공합니다.
 *
 * @author Gwanggeun Yoo
 */
public class ResourceScanner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceScanner.class);
    private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);

    /**
     * {@link #scanByAnnotation(ClassPathScanningCandidateComponentProvider, String, Class[])} 메소드를 참고하세요.
     */
    @SuppressWarnings("unchecked")
    public static Set<BeanDefinition> scan(String basePackage, Class<? extends Annotation>... annotationTypes)
    {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        return scanByAnnotation(scanner, basePackage, annotationTypes);
    }

    /**
     * Annotation을 포함하는 클래스를 검색하여 줍니다.
     *
     * @param scanner 스캔주체 클래스.
     * @param basePackage 스캔하려는 상위 패키지.
     * @param annotationTypes 스캔하려는 Annotation 배열.
     * @return BeanDefinition 집합.
     */
    @SafeVarargs
    public static Set<BeanDefinition> scanByAnnotation(ClassPathScanningCandidateComponentProvider scanner,
        String basePackage, Class<? extends Annotation>... annotationTypes)
    {
        if (annotationTypes != null && annotationTypes.length > 0)
        {
            for (int i = 0; i < annotationTypes.length; i++)
            {
                scanner.addIncludeFilter(new AnnotationTypeFilter(annotationTypes[i]));
            }
        }
        Set<BeanDefinition> definitions = scanner.findCandidateComponents(basePackage);
        return definitions;
    }

    /**
     * {@link #scanByAssignableFilter(ClassPathScanningCandidateComponentProvider, String, Class)} 참조.
     */
    public static Set<BeanDefinition> scanByAssignableFilter(String basePackage, Class<?> parentType)
    {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        return scanByAssignableFilter(scanner, basePackage, parentType);
    }

    /**
     * 할당가능한 클래스를 검색하여 줍니다.
     *
     * @param scanner 스캔 객체
     * @param basePackage 스캔하고자 하는 상위 패키지.
     * @param parentType 할당 가능한 부모 클래스 또는 인터페이스 유형,
     * @return BeanDefinition 집합.
     */
    public static Set<BeanDefinition> scanByAssignableFilter(ClassPathScanningCandidateComponentProvider scanner,
        String basePackage, Class<?> parentType)
    {
        if (parentType == null)
        {
            return new LinkedHashSet<>();
        }
        scanner.addIncludeFilter(new AssignableTypeFilter(parentType));
        Set<BeanDefinition> definitions = scanner.findCandidateComponents(basePackage);
        return definitions;
    }
    
    /**
     * 패턴 기반으로 리소스를 찾아 줍니다.
     *
     * <br>
     *
     * 예) Resource[] resources = findResources("classpath:com/macrogen/** /*.class");
     *
     * @param pattern - ant style pattern string.
     * @return 패턴에 맞는 리소스를 모두 찾아 배열로 반환하여 줍니다.
     */
    public static Resource[] findResources(String pattern)
    {
        Resource[] res = null;
        try
        {
            res = resolver.getResources(pattern);
        } catch (IOException e)
        {
            LOGGER.info("Error : ", e);
        }
        return res;
    }

    /**
     * 단일 리소스를 찾아 줍니다.
     *
     * @param location 찾으려는 리소스 위치.
     * @return 찾은경우 해당 Resource 객체, 못찾으면 null.
     */
    public static Resource findResource(String location)
    {
        return resolver.getResource(location);
    }

    public static BeanDefinition createBeanDefinition(Class<?> clz)
    {
        MetadataReader metadataReader = null;
        try
        {
            metadataReader = metadataReaderFactory.getMetadataReader(clz.getName());
        } catch (IOException e)
        {
            LOGGER.info("Error : ", e);
        }
        if (metadataReader == null)
        {
            return null;
        }

        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
        sbd.setResource(metadataReader.getResource());
        sbd.setSource(metadataReader.getResource());
        return sbd;
    }
}
