package com.dailystudy.swinglab.service.framework.utils;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.framework.auth.SwinglabUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.sasl.AuthenticationException;

/**
 * {@link SecurityContextHolder}에 담긴 인증객체를 다루는 유틸리티
 */
public class SecurityUtil
{
    /**
     * 인증객체 세팅
     *
     * @param authentication
     */
    public static void setAuthentication (Authentication authentication)
    {
        if (authentication == null)
        {
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 로그인 인증된 사용자의 user 아이디 추출
     *
     * @return
     * @throws AuthenticationException
     */
    public static Long getUserId ()
    {
        return SecurityUtil.getUserInfo().getUserId();
    }

    /**
     * 로그인 인증된 사용자의 정보 추출
     * @return
     */
    public static User getUserInfo ()
    {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context != null ? context.getAuthentication() : null;
        if (authentication == null)
        {
            throw new RuntimeException("로그인 정보가 없습니다.");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null)
        {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        return ((SwinglabUserDetail) userDetails).getUser();
    }
}
