package com.dailystudy.swinglab.service.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Slf4j
public class ReflectionUtil
{
    private static final Class<?>[] EMPTY_CLAZZ = new Class<?>[] {};

    public static Map<String, PropertyDescriptor> mappingColumn(String[] columnList, Class<?> clazz)
    {
        Map<String, PropertyDescriptor> propertyDescriptorMap = new HashMap<>();

        List<PropertyDescriptor>
            propertyDescriptorList = getPropertyDescriptors(clazz);

        for (String col : columnList) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptorList) {
                if (col.equals(propertyDescriptor.getName())) {
                    propertyDescriptorMap.put(col, propertyDescriptor);
                }
            }
        }
        return propertyDescriptorMap;
    }
    
    /**
     * String class도 primitive 타입으로 처리한다.
     * 
     * @param clz 비교 클래스.
     * @return String, primitive 타입 또는 primitive wrapper 클래스인 경우 true 를 리턴, 아니면 false.
     */
    public static boolean isPrimitive(Class<?> clz)
    {
        if (clz.isPrimitive() == true)
            return true;
        return clz == Integer.class || clz == Byte.class || clz == Short.class || clz == Double.class
            || clz == Float.class || clz == Long.class || clz == Boolean.class || clz == String.class;
    }

    /**
     * String 및 String 관련 클래스 확인.
     * @param clz
     * @return String, StringBuffer 또는 StringBuilder 클래스인경우 true, 아니면 false.
     */
    public static boolean isStringType(Class<?> clz)
    {
        if (clz == null)
        {
            return false;
        }
        return clz == String.class || clz == StringBuffer.class || clz == StringBuilder.class;
    }

    /**
     * 배열의 길이를 구해준다.
     * @param arrObj Array로 추정되는 객체.
     * @return Array 의 length.
     */
    public static int getArraySize(Object arrObj)
    {
        if (arrObj == null)
        {
            return -1;
        }
        return Array.getLength(arrObj);
    }

    /**
     * 이름기반으로 필드를 찾아준다.
     *
     * @param clz 클래스 객체.
     * @param fieldName 찾고자 하는 필드 객체.
     * @return 필드 데이터.
     * @throws NoSuchFieldException 필드를 찾지 못한경우 예외를 발생.
     */
    public static Field findField(Class<?> clz, String fieldName) throws NoSuchFieldException
    {
        Class<?> current = clz;
        while (current != null && current != Object.class)
        {
            Field[] fs = current.getDeclaredFields();
            for (Field f : fs)
            {
                if (f.getName().equals(fieldName))
                {
                    f.setAccessible(true);
                    return f;
                }
            }
            current = current.getSuperclass();
        }
        String clzName = clz == null ? "null class" : clz.getName();
        throw new NoSuchFieldException("Not found field. fieldName:" + fieldName + " in class:" + clzName);
    }

    /**
     * 필드에 정의된 파라미터 타입을 찾아 준다.
     *
     * @param f Field객체.
     * @return Field객체가 parameterized type일 경우 정의된 클래스 배열, 그렇지 않은경우 EMPTY Array 가 반환된다.
     */
    public static Class<?>[] getParameterizedTypes(Field f)
    {
        Type type = f.getGenericType();
        return getParameterizedTypes(type);
    }

    public static Class<?>[] getParameterizedTypes(Type type)
    {
        if (type instanceof ParameterizedType)
        {
            Type[] pTypes = ((ParameterizedType) type).getActualTypeArguments();
            Class<?>[] arr = new Class<?>[pTypes.length];
            int i = 0;
            for (Type pType : pTypes)
            {
                arr[i++] = (Class<?>) pType;
                // System.out.println(pType);
            }
            return arr;
        }
        return EMPTY_CLAZZ;
    }

    /**
     * static 으로 선언되지 않은 모든 필드를 구해준다.
     *
     * @param clz 클래스 객체
     * @return static으로 선언되지 않은 모든 필드 리스트.
     */
    public static List<Field> getNonStaticFields(Class<?> clz)
    {
        List<Field> fields = new LinkedList<>();
        Class<?> current = clz;
        while (current != Object.class)
        {
            Field[] fs = current.getDeclaredFields();
            for (Field f : fs)
            {
                if (java.lang.reflect.Modifier.isStatic(f.getModifiers()))
                {
                    continue;
                }
                fields.add(f);
            }
            if (current.getSuperclass() == null)
            {
                log.debug("Super class is null for class > {}", current.getName());
                break;
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    /**
     * Boolean유형의 클래스인지 확인한다.
     * @param clz 클래스.
     * @return Boolean 또는 boolean이면 true.
     */
    public static boolean isBooleanType(Class<?> clz)
    {
        return clz == boolean.class || clz == Boolean.class;
    }

    /**
     * Date 유형의 클래스 인지 확인하여 줍니다.
     *
     * @param clz 비교 클래스.
     * @return Date, Calendar 또는 Calendar에 할당할 수 있는 클래스 이면 true.
     */
    public static boolean isDateType(Class<?> clz)
    {
        return clz == Date.class || clz == java.sql.Date.class || Calendar.class.isAssignableFrom(clz);
    }

    /**
     * Array또는 List인지 확인하여 줍니다.
     *
     * @param clz 비교 클래스.
     * @return Array 또는 List객체이면 true.
     */
    public static boolean isArrayOrListType(Class<?> clz)
    {
        return List.class.isAssignableFrom(clz) || clz.isArray();
    }

    /**
     * String값을 원하는 primitive값으로 변환하여 줍니다.
     *
     * @param clz 변경하고자 하는 타입.
     * @param value 문자열 값.
     * @return 입력받은 유형으로 변환한 값, 변환 실패시 null.
     */
    public static Object convertString2PrimitiveTypeValue(Class<?> clz, String value)
    {
        if (value == null)
            return value;
        if (clz == String.class)
        {
            return value;
        } else if (clz == int.class || clz == Integer.class)
        {
            return Integer.parseInt(value.trim());
        } else if (clz == byte.class || clz == Byte.class)
        {
            return Byte.parseByte(value.trim());
        } else if (clz == short.class || clz == Short.class)
        {
            return Short.parseShort(value.trim());
        } else if (clz == double.class || clz == Double.class)
        {
            return Double.parseDouble(value.trim());
        } else if (clz == float.class || clz == Float.class)
        {
            return Float.parseFloat(value.trim());
        } else if (clz == long.class || clz == Long.class)
        {
            return Long.parseLong(value.trim());
        } else if (clz == boolean.class || clz == Boolean.class)
        {
            return Boolean.valueOf(value.trim());
        }
        if (log.isDebugEnabled())
        {
            log.debug("type:{} is not primitive type", clz.getName());
        }
        return null;
    }

    /**
     * 클래스 안에 특정 Type이 속해 있는지 확인하여 줍니다.
     *
     * @param pType get/set을 포함한 메인 클래스.
     * @param checkType 검사 하려는 유형.
     * @return checkType이 pType에 속해있으면 true, 아니면 false.
     */
    public static boolean isIncludedInType(Class<?> pType, Class<?> checkType)
    {
        BeanInfo beanInfo;
        try
        {
            beanInfo = Introspector.getBeanInfo(pType);
            if (beanInfo != null)
            {
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                if (pds != null)
                {
                    for (PropertyDescriptor pd : pds)
                    {
                        if (pd.getPropertyType() == checkType)
                        {
                            return true;
                        }
                    }
                }
            }
        } catch (IntrospectionException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn(e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * 클래스에 속한 Property Descriptor 목록을 구해 줍니다.
     *
     * @param clazz 검색하는 클래스.
     * @return 클래스에 속한 모든 프로퍼티 목록 ({@link PropertyDescriptor} List)
     */
    public static List<PropertyDescriptor> getPropertyDescriptors(Class<?> clazz)
    {
        if (clazz == null)
        {
            return null;
        }

        if (ReflectionUtil.isPrimitive(clazz))
        {
            return null;
        }

        List<PropertyDescriptor> propertyList = new ArrayList<>();

        try
        {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            if (beanInfo != null)
            {
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                if (descriptors != null)
                {
                    for (PropertyDescriptor descriptor : descriptors)
                    {
                        if (descriptor.getPropertyType() == Class.class)
                        {
                            continue;
                        }
                        propertyList.add(descriptor);
                    }
                }
            }
        } catch (IntrospectionException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn(e.getMessage(), e);
            }
        }

        return propertyList;
    }
}
