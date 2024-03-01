package com.dailystudy.swinglab.service.framework.http.uris;

/**
 * 인증, 권한관련 URI
 */
public class AuthUriConts {
    public static final String POST_SIGNUP = "/v1/auth/signup"; // 회원가입
    public static final String POST_LOGIN = "/v1/auth/login"; // 로그인
    public static final String POST_LOGOUT = "/v1/auth/logout"; // 로그아웃
    public static final String POST_LOGIN_REFRESH = "/v1/auth/login/refresh"; // 로그인 갱신
}
