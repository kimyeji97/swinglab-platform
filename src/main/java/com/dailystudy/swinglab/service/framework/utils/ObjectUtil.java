package com.dailystudy.swinglab.service.framework.utils;

import com.dailystudy.swinglab.service.framework.http.response.exception.SwinglabRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Object 관련 유틸리티
 *
 * @author yjkim
 */
@Slf4j
public class ObjectUtil
{
    private static final String PATTERN_LIST_INDEX = "\\[[0-9]+\\]";
    private static final String SPLIT_CHAR_KEY = "\\.";
    private static final String REX_TIMESTAMP = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
    private static final String REX_DATE = "\\d{4}-\\d{2}-\\d{2}";
    private static final String PIN_DATE = "yyyy-MM-dd";
    private static final String PTN_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
    private static final Map<String, Object> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>());

    /**
     * Map에서 key의 value값을 Object로 리턴
     *
     * @param object
     * @param key
     * @return
     */
    public static Object findMapValueByKey(Map object, String key)
    {
        if (ObjectUtils.isEmpty(object))
        {
            throw new SwinglabRuntimeException("The target Map object is empty.");
        }
        if (StringUtils.isEmpty(key))
        {
            throw new SwinglabRuntimeException("The Key is empty.");
        }

        Object value = null;

        Set<String> keySet = object.keySet();
        if (keySet.contains(key))
        {
            value = object.get(key);
            if (value instanceof Map)
            {
                value = (Map<String, Object>) value;
            } else if (value instanceof String)
            {
                value = String.valueOf(value);
            } else if (value instanceof Integer)
            {
                value = (Integer) value;
            } else if (value instanceof Boolean)
            {
                value = (Boolean) value;
            } else if (value instanceof List)
            {
                value = (List) value;
            }
        } else
        {
            throw new SwinglabRuntimeException("Not found the " + key);
        }

        return value;
    }

    /**
     * List에서 해당 index에 존재하는 값을 Object로 리턴
     *
     * @param list
     * @param index
     * @return
     */
    public static Object findListValueByIndex(List list, Integer index)
    {
        if (ObjectUtils.isEmpty(list))
        {
            throw new SwinglabRuntimeException("The find target list is empty.");
        }

        if (ObjectUtils.isEmpty(index))
        {
            throw new SwinglabRuntimeException("The index is empty.");
        }

        if (list.size() <= index)
        {
            throw new SwinglabRuntimeException("index more then list size.");
        }
        return list.get(index);
    }

    /**
     * object에서 path의 값을 구한다.
     * path가 존재하지 않는다면 {@link SwinglabRuntimeException} throw한다.
     * ex) path : key1.key2[0].key3
     *
     * @param obj
     * @param path
     * @return
     */
    public static Object getValueByPath(Object obj, String path)
    {

        Object value = obj;

        List<String> paths = Arrays.asList(path.split(SPLIT_CHAR_KEY));
        Pattern pattern = Pattern.compile(PATTERN_LIST_INDEX);

        for (String p : paths)
        {
            Matcher matcher = pattern.matcher(p);
            if (matcher.find())
            {
                String mapKey = p.substring(0, matcher.start());
                if (StringUtils.isNotBlank(mapKey))
                {
                    if (value instanceof Map)
                    {
                        value = ObjectUtil.findMapValueByKey((Map) value, mapKey);
                    } else
                    {
                        throw new SwinglabRuntimeException("The target object is not Map.");
                    }
                }

                matcher.reset();
                while (matcher.find())
                {
                    String group = matcher.group();
                    int idx = Integer.parseInt(group.substring(1, group.length() - 1));
                    if (obj instanceof List)
                    {
                        value = ObjectUtil.findListValueByIndex((List) value, idx);
                    } else
                    {
                        throw new SwinglabRuntimeException("The target object is not List.");
                    }
                }
            } else
            {
                if (value instanceof Map)
                {
                    value = ObjectUtil.findMapValueByKey((Map) value, p);
                } else
                {
                    throw new SwinglabRuntimeException("The target object is not Map.");
                }
            }
        }

        return value;
    }

    /**
     * object에 path가 존재하는지 확인한다.
     * true : 존재, false : 존재하지 않음
     * <p>
     * {@link ObjectUtil#getValueByPath(Object, String)}에서
     * {@link SwinglabRuntimeException}이 throw되면 존재하지 않은 것.
     *
     * @param obj
     * @param path
     * @return
     */
    public static boolean existPath(Object obj, String path)
    {
        if (ObjectUtils.isEmpty(obj) || ObjectUtils.isEmpty(path))
        {
            return false;
        }

        try
        {
            ObjectUtil.getValueByPath(obj, path);
            return true;
        } catch (SwinglabRuntimeException e)
        {
            return false;
        }
    }

    /**
     * 두개의 Object를 비교해서 다른 new값을 리턴한다.
     *
     * @param obj1 (old)
     * @param obj2 (new)
     * @return Map<String, String>
     */
    public static Map<String, Object> compareObject(Object obj1, Object obj2)
    {
        if (ObjectUtils.isEmpty(obj1) || ObjectUtils.isEmpty(obj2) || obj1.getClass() != obj2.getClass())
        {
            return EMPTY_MAP;
        }
        return ObjectUtil.compareMapObject(ObjectUtil.converObjectToMap(obj1), ObjectUtil.converObjectToMap(obj2));
    }

    /**
     * 두개의 Map을 비교해서 다른 new값을 리턴한다.
     *
     * @param map1 (old)
     * @param map2 (new)
     * @return Map<String, Object>
     */
    public static Map<String, Object> compareMapObject(Map<String, Object> map1, Map<String, Object> map2)
    {
        Map<String, Object> result = new HashMap<>();
        // map2를 map1과 비교해서 다른 (map2)값을 리턴한다.
        if (ObjectUtils.isEmpty(map1) || ObjectUtils.isEmpty(map2))
        {
            return EMPTY_MAP;
        }

        for (Map.Entry<String, Object> entry1 : map1.entrySet())
        {
            String key1 = entry1.getKey();
            if (!String.valueOf(entry1.getValue()).equals(String.valueOf(map2.get(key1))))
            {
                result.put(key1, map2.get(key1));
            }
        }

        return result;
    }

    /**
     * Object의 특정 Setter 메소드 실행
     *
     * @param obj
     * @param m
     * @param val
     */
    public static void invokeMethodSiently(Object obj, Method m, Object val)
    {
        if (obj == null || m == null)
        {
            return;
        }
        try
        {
            m.invoke(obj, val);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex)
        {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * Object의 Getter 메소드 실행
     *
     * @param o
     * @param m
     * @return
     */
    private static Object invokeGetMethod(Object o, Method m)
    {
        if (o == null || m == null)
        {
            return null;
        }
        try
        {
            Object obj = m.invoke(o);
            if (ObjectUtils.isEmpty(obj))
            {
                return null;
            } else
            {
                return obj;
            }
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex)
        {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Object를 Map으로 변환한다.
     *
     * @param obj
     * @return Map<String, String>
     * @throws JsonProcessingException
     */
    public static Map<String, Object> converObjectToMap(Object obj)
    {
        if (obj == null)
        {
            return new HashMap<>();
        }
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            List<PropertyDescriptor> propertyDescriptors = ReflectionUtil.getPropertyDescriptors(obj.getClass());
            if (propertyDescriptors == null)
            {
                return resultMap;
            }

            for (PropertyDescriptor pd : propertyDescriptors)
            {
                Object value = invokeGetMethod(obj, pd.getReadMethod());
                resultMap.put(pd.getName(), value);
            }
        } catch (IllegalArgumentException e)
        {
            log.trace(e.getMessage(), e);
        }
        return resultMap;
    }

    /**
     * 해당 클래스가 가진 모든 필드를 리스트로 리턴
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<Field> getAllFields(T t)
    {
        Objects.requireNonNull(t);

        Class<?> clazz = t.getClass();
        List<Field> fields = new ArrayList<>();
        while (clazz != null)
        {    // 1. 상위 클래스가 null 이 아닐때까지 모든 필드를 list 에 담는다.
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 해당 클래스에서 fieldName을 가진 항목을 Field 타입으로 리턴
     *
     * @param t
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> Field getFieldByName(T t, String fieldName)
    {
        Objects.requireNonNull(t);

        Field field = null;
        for (Field f : getAllFields(t))
        {
            if (f.getName().equals(fieldName))
            {
                field = f;    // 2. 모든 필드들로부터 fieldName이 일치하는 필드 추출
                break;
            }
        }
        if (field != null)
        {
            field.setAccessible(true);    // 3. 접근 제어자가 private 일 경우
        }
        return field;
    }

    /**
     * 해당 클래스의 해당 key가 가진 모든 Annotation을 리턴
     *
     * @param t
     * @param key
     * @param <T>
     * @return
     */
    public static <T> List<Annotation> getFieldAnnotations(T t, String key)
    {
        if (t == null || ObjectUtils.isEmpty(key))
        {
            return null;
        }

        List<Field> lf = getAllFields(t);
        for (Field f : lf)
        {
            if (f.getName().equals(key))
            {
                return Arrays.asList(f.getAnnotations());
            }
        }

        return null;
    }

    public static void cloneObject(Object src, Object target)
    {
        if (src == null || target == null)
        {
            return;
        }

        List<PropertyDescriptor> srcPropertyDescriptors = ReflectionUtil.getPropertyDescriptors(src.getClass());
        List<PropertyDescriptor> targetPropertydescriptors = ReflectionUtil.getPropertyDescriptors(target.getClass());

        ObjectUtil.cloneObject(srcPropertyDescriptors, src, targetPropertydescriptors, target);
    }

    public static void cloneObject(List<PropertyDescriptor> srcPdList, Object src,
        List<PropertyDescriptor> targetPdList, Object target)
    {
        if (targetPdList == null || srcPdList == null || target == null || src == null)
        {
            return;
        }

        Map<String, PropertyDescriptor> tPdMap = new HashMap<>();
        for (PropertyDescriptor tPd : targetPdList)
        {
            tPdMap.put(tPd.getName(), tPd);
        }

        for (PropertyDescriptor sPd : srcPdList)
        {
            if (tPdMap.containsKey(sPd.getName()) == false || sPd.getReadMethod() == null)
            {
                continue;
            }

            PropertyDescriptor propertyDescriptor = tPdMap.get(sPd.getName());
            try
            {
                Object value = sPd.getReadMethod().invoke(src);
                propertyDescriptor.getWriteMethod().invoke(target, value);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                log.error(e.getMessage(), e);
            }
        }

    }
}
