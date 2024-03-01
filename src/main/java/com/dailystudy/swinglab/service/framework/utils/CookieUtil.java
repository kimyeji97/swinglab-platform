package com.dailystudy.swinglab.service.framework.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * Cookie 유틸리티
 */
public class CookieUtil
{

    /**
     * Response에 쿠키 세팅
     *
     * @param response
     * @param key
     * @param value
     * @param expSec
     */
    public static void addCookie (HttpServletResponse response, String key, String value, int expSec)
    {
        if (StringUtils.isBlank(key))
        {
            return;
        }
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expSec);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * Response에 쿠키 제거
     *
     * @param response
     * @param key
     */
    public static void removeCookie (HttpServletResponse response, String key)
    {
        if (StringUtils.isBlank(key))
        {
            return;
        }
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
