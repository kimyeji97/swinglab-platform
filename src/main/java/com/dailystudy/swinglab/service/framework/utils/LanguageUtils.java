package com.dailystudy.swinglab.service.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class LanguageUtils
{

    public static void applyLanguage(Object lang, Object target)
    {
        if (lang == null || target == null)
        {
            return;
        }

        List<PropertyDescriptor> langPropertyDescriptors = ReflectionUtil.getPropertyDescriptors(lang.getClass());
        List<PropertyDescriptor> targetPropertyDescriptors = ReflectionUtil.getPropertyDescriptors(target.getClass());

        LanguageUtils.applyLanguage(langPropertyDescriptors, lang, targetPropertyDescriptors, target);
    }

    public static void applyLanguage(List<PropertyDescriptor> langPdList, Object lang,
        List<PropertyDescriptor> targetPdList, Object target)
    {
        if (langPdList == null || targetPdList == null || lang == null || target == null)
        {
            return;
        }

        Map<String, PropertyDescriptor> tPdMap = new HashMap<>();
        for (PropertyDescriptor tPd : targetPdList)
        {
            tPdMap.put(tPd.getName(), tPd);
        }

        for (PropertyDescriptor lPd : langPdList)
        {
            if (lPd.getPropertyType() != String.class || tPdMap.containsKey(lPd.getName()) == false || lPd.getReadMethod() == null)
            {
                continue;
            }

            PropertyDescriptor propertyDescriptor = tPdMap.get(lPd.getName());
            try
            {
                String value = (String) lPd.getReadMethod().invoke(lang);
                propertyDescriptor.getWriteMethod().invoke(target, value);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                log.error(e.getMessage(), e);
            }
        }

    }
}

