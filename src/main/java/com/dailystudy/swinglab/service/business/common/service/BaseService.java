package com.dailystudy.swinglab.service.business.common.service;

import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabBadRequestException;
import com.dailystudy.swinglab.service.framework.utils.StringValidUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class BaseService {

    public void assertNotNull(Object target, String params)
    {
        if (target == null)
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",params,"'은(는) 올바른 값이 아닙니다."));
        }
    }

    public void assertNotBlank(Object target, String params)
    {
        if (target == null || StringUtils.isBlank(target.toString()))
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",params,"' 값이 비어 있습니다."));
        }
    }

    public void assertNotEmpty(Object target, String params)
    {
        if (ObjectUtils.isEmpty(target))
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",params,"' 값이 비어 있습니다."));
        }
    }

    protected void assertNotEmpty(List<?> target, String params)
    {
        if (target == null || target.isEmpty())
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",params,"' 값이 비어 있습니다."));
        }
    }

    protected void assertNotEmpty(Object[] target, String params)
    {
        if (target == null || target.length == 0)
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",params,"' 값이 비어 있습니다."));
        }
    }

    protected void assertNotEmptyOr(Object... params)
    {
        boolean notAllNull = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.length; i = i + 2)
        {
            sb.append(params[i + 1].toString());
            if (ObjectUtils.isNotEmpty(params[i]))
            {
                notAllNull = true;
                break;
            }
        }

        if (!notAllNull)
        {
            sb = sb.deleteCharAt(sb.length() - 1);
            throw new SwinglabBadRequestException(StringUtils.join("'",params,"'이(가) 모두 null입니다."));
        }
    }

    protected void assertIn(Object target, Object[] container, String... params)
    {
        List<Object> list = Arrays.asList(container);
        if (!list.contains(target))
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",params,"' 값이 비어 있습니다."));
        }
    }

    protected void assertMaxLength(String str, int max, String param)
    {
        boolean valid = StringValidUtil.checkMaxLength(str, max);
        if(valid == false)
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",param,"'은(는) 최대 ", max,"자 까지 입력해 주시기 바랍니다."));
        }
    }

    protected void assertMinLength(String str, int min, String param)
    {
        boolean valid = StringValidUtil.checkMinLength(str, min);
        if(valid == false)
        {
            throw new SwinglabBadRequestException(StringUtils.join("'",param,"'은(는) 최소 ", min,"자 까지 입력해 주시기 바랍니다."));
        }
    }
}
