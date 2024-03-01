package com.dailystudy.swinglab.service.framework.utils;


import com.dailystudy.swinglab.service.framework.http.response.exception.SwinglabInitializingFailedException;

import java.lang.annotation.Annotation;

/** 
 * AnnotationUtil 클래스 
 * 
 */	
public class AnnotationUtil {
	
	/** 
   	 * Annotation을 찾는다.
   	 * 
   	 * @param className 클래스명 
   	 * @param annotationType annotationType
   	 */
    public static <A extends Annotation> A findAnnotation(String className, Class<A> annotationType) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getAnnotation(annotationType);
        } catch (ClassNotFoundException e) {
            throw new SwinglabInitializingFailedException("Cannot find class -> " + className, e);
        }
    }
    
    /** 
   	 * Annotation을 찾는다.
   	 * 
   	 * @param className 클래스명 
   	 * @param annotationType annotationType
   	 */
    public static <A extends Annotation> A[] findAnnotations(String className, Class<A> annotationType) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getAnnotationsByType(annotationType);
        } catch (ClassNotFoundException e) {
            throw new SwinglabInitializingFailedException("Cannot find class -> " + className, e);
        }
    }
}
