package com.dailystudy.swinglab.service.business.auth.controller;

import com.dailystudy.swinglab.service.business.auth.service.AuthService;
import com.dailystudy.swinglab.service.business.common.domain.JwtToken;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.auth.JwtTokenProvider;
import com.dailystudy.swinglab.service.framework.http.response.PlatformResponseBuilder;
import com.dailystudy.swinglab.service.framework.http.response.domain.SuccessResponse;
import com.dailystudy.swinglab.service.framework.http.uris.AuthUriConts;
import com.dailystudy.swinglab.service.framework.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 회원가입
     *
     * @param user
     * @return
     */
    @PostMapping(AuthUriConts.POST_SIGNUP)
    public ResponseEntity<SuccessResponse<User>> postSignup (@RequestBody User user)
    {
        return PlatformResponseBuilder.build(authService.signupUser(user));
    }

    /**
     * 로그인
     *
     * @param user
     * @param response
     * @return
     */
    @PostMapping(AuthUriConts.POST_LOGIN)
    public ResponseEntity<SuccessResponse<JwtToken>> postLogin (@Valid @RequestBody User user, HttpServletResponse response)
    {
        log.debug("[LOGIN] loginId : {} / encode pwd : {} / decode pwd : {}", user.getLoginId(), user.getPwd(), new String(Base64.decodeBase64(user.getPwd())));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getLoginId(), new String(Base64.decodeBase64(user.getPwd())));
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtToken result = jwtTokenProvider.generateJwtToken(user);
        response.addHeader(SwinglabConst.AUTHORIZATION_HEADER, StringUtils.join(SwinglabConst.BEARER_TOKEN, " ", result.getAccessToken()));
        //        CookieUtil.addCookie(response, SwinglabConst.COOKIE_REFRESH_TOKEN_KEY, result.getRefreshToken(), result.getRefreshExpSec());

        // redis에 리프레시 토큰 저장
        redisTemplate.opsForValue().set(StringUtils.join(SwinglabConst.REDIS_KEY_LOGIN_REFRESH_TOKEN,":",SecurityUtil.getUserId()),
                result.getRefreshToken(),
                jwtTokenProvider.getExpirationFromToken(result.getRefreshToken()),
                TimeUnit.MILLISECONDS
        );

        return PlatformResponseBuilder.build(result);
    }

    /**
     * access token 재발급
     *
     * @param token
     * @param response
     * @return
     */
    @PostMapping(AuthUriConts.POST_LOGIN_REFRESH)
    public ResponseEntity<SuccessResponse<JwtToken>> postLoginRefreshToken (@RequestBody JwtToken token, HttpServletResponse response)
    {
        JwtToken result = jwtTokenProvider.refreshAccessToken(token.getRefreshToken());
        response.addHeader(SwinglabConst.AUTHORIZATION_HEADER, StringUtils.join(SwinglabConst.BEARER_TOKEN, " ", result.getAccessToken()));
        return PlatformResponseBuilder.build(result);
    }

    /**
     * 로그아웃 처리
     *
     * @param token
     * @return
     */
    @PostMapping(AuthUriConts.POST_LOGOUT)
    public ResponseEntity<SuccessResponse> postLogout (HttpServletRequest request)
    {
        Long userId = SecurityUtil.getUserId();
        // Refresh Token을 삭제
        String key = StringUtils.join(SwinglabConst.REDIS_KEY_LOGIN_REFRESH_TOKEN,":",userId);
        if (redisTemplate.opsForValue().get(key) != null)
        {
            redisTemplate.delete(key);
        }

        // redis에 로그아웃 저장 (access 토큰 유효시간만큼 지정)
        String accessToken = jwtTokenProvider.getTokenFromHeader(request.getHeader(SwinglabConst.AUTHORIZATION_HEADER));
        long expirationTime = jwtTokenProvider.getExpirationFromToken(accessToken);
        redisTemplate.opsForValue().set(StringUtils.join(SwinglabConst.REDIS_KEY_LOGOUT_ACCESS_TOKEN,":",accessToken), SwinglabConst.REDIS_VALUE_LOGOUT, expirationTime, TimeUnit.MILLISECONDS);
        return PlatformResponseBuilder.build();
    }
}
