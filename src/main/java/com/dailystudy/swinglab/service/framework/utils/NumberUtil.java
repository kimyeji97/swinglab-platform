package com.dailystudy.swinglab.service.framework.utils;

import java.math.BigDecimal;

public class NumberUtil
{

    public static Boolean isInculdeNumberRnage(Integer num, Integer s, Integer e)
    {
        if (num == null || (s == null && e == null))
        {
            return null;
        }

        if (s == null)
        {
            return num <= e;
        }

        if (e == null)
        {
            return s <= num;
        }

        return s <= num && num <= e;
    }

    public static Double convertNullToZero(Double val)
    {
        if (val == null)
        {
            return 0d;
        }
        return val;
    }

    public static Double convertNullOrMinusToZero(Double val)
    {
        Double result = convertNullToZero(val);
        return result < 1 ? 0 : result;
    }

    public static Integer convertNullToZero(Integer val)
    {
        if (val == null)
        {
            return 0;
        }
        return val;
    }

    public static Integer convertNullOrMinusToZero(Integer val)
    {
        Integer result = convertNullToZero(val);
        return result < 1 ? 0 : result;
    }

    public static Double round(Double d, int n)
    {
        if (d == null || n < 0)
        {
            return d;
        }
        double m = Math.pow(10.0, n);
        return Math.round(d * m) / m;
    }

    public static BigDecimal convertNullToZero(BigDecimal val)
    {
        if (val == null)
        {
            return BigDecimal.ZERO;
        }
        return val;
    }

    public static BigDecimal convertNullOrMinusToZero(BigDecimal val)
    {
        BigDecimal result = convertNullToZero(val);
        return (result.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : result;
    }
}
