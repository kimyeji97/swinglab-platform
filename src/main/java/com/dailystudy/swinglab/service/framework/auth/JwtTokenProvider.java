package com.dailystudy.swinglab.service.framework.auth;

import com.dailystudy.swinglab.service.business.auth.service.SwinglabUserDetailsService;
import com.dailystudy.swinglab.service.business.common.domain.JwtToken;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabUnauthorizedException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dailystudy.swinglab.service.framework.core.SwinglabConst.ACCESS_TOKEN_EXPIRE_TIME;
import static com.dailystudy.swinglab.service.framework.core.SwinglabConst.REFRESH_TOKEN_EXPIRE_TIME;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider
{
    @Value("${security.jwt.key}")
    private String securityJwtKey;

    private final UserRepository userRepository;
    private final SwinglabUserDetailsService swinglabUserDetailsService;

    @PostConstruct
    protected void init ()
    {
        securityJwtKey = Base64.getEncoder().encodeToString(securityJwtKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Token 생성
     *
     * @param user
     * @return
     */
    public JwtToken generateJwtToken (User user)
    {
        long todayTime = new Date().getTime();
        Map<String, Object> header = this.createHeader();
        Map<String, Object> claims = this.createClaims(user);

        String accessToken = generateToken(header, claims, user.getUserId(), new Date(todayTime + ACCESS_TOKEN_EXPIRE_TIME));
        String refreshToken = generateToken(header, claims, user.getUserId(), new Date(todayTime + REFRESH_TOKEN_EXPIRE_TIME));

        JwtToken result = new JwtToken();
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        result.setRefreshExpSec((int) REFRESH_TOKEN_EXPIRE_TIME / 1000);
        return result;
    }

    /**
     * Access token 재발급
     *
     * @param token
     * @return
     */
    public JwtToken refreshAccessToken (String token)
    {
        SwinglabConst.JWT_EXCEPT jwtExcept = this.isValidToken(token);
        if (jwtExcept != null)
        {
            log.error("The refreshToken is invalid : {}", jwtExcept);
            throw new SwinglabUnauthorizedException(jwtExcept.getTitle(), jwtExcept.getErrorMessage());
        }

        String loginId = this.getLoginIdFromToken(token);
        User user = userRepository.findByLoginIdAndDelYnFalse(loginId);
        return this.generateJwtToken(user);
    }

    /**
     * JWT의 "헤더" 값을 생성해주는 메서드
     *
     * @return HashMap<String, Object>
     */
    private Map<String, Object> createHeader ()
    {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    /**
     * 사용자 정보를 기반으로 클래임을 생성해주는 메서드
     *
     * @param user 사용자 정보
     * @return Map<String, Object>
     */
    private Map<String, Object> createClaims (User user)
    {
        // 공개 클레임에 사용자의 이름과 이메일을 설정하여 정보를 조회할 수 있다.
        Map<String, Object> claims = new HashMap<>();

        log.info("loginId :" + user.getLoginId());
        log.info("userNm :" + user.getName());

        claims.put(SwinglabConst.USER_ID, user.getUserId());
        claims.put(SwinglabConst.LOGIN_ID, user.getLoginId());
        claims.put(SwinglabConst.USER_NM, user.getName());
        return claims;
    }

    /**
     * 사용자 정보를 기반으로 토큰을 생성하여 반환 해주는 메서드
     *
     * @param header
     * @param claims
     * @param userId
     * @param expDate
     * @return
     */
    public String generateToken (Map<String, Object> header, Map<String, Object> claims, Long userId, Date expDate)
    {
        // 사용자 시퀀스를 기준으로 JWT 토큰을 발급하여 반환해줍니다.
        JwtBuilder builder = Jwts.builder()
                .setHeader(header)  // Header
                .setClaims(claims)  // Payload - Claims 구성
                .setSubject(String.valueOf(userId)) // Payload - Subject 구성
                .signWith(SignatureAlgorithm.HS256, createSignature())  // Signature 구성
                .setIssuedAt(new Date()) // 발생시간
                .setExpiration(expDate); // Expired Date 구성
        return builder.compact();
    }

    /**
     * JWT "서명(Signature)" 발급을 해주는 메서드
     *
     * @return Key
     */
    private Key createSignature ()
    {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(securityJwtKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Header 내에 토큰을 추출합니다.
     *
     * @param header 헤더
     * @return String
     */
    public String getTokenFromHeader (String header)
    {
        if (!header.startsWith(SwinglabConst.BEARER_TOKEN))
        {
            return StringUtils.EMPTY;
        }
        return header.split(" ")[1];
    }

    /**
     * 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     *
     * @param token : 토큰
     * @return Claims : Claims
     */
    private Claims getClaimsFormToken (String token)
    {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(securityJwtKey))
                .parseClaimsJws(token).getBody();
    }

    /**
     * 토큰을 기반으로 사용자 정보를 반환받는 메서드
     *
     * @param token : 토큰
     * @return String : 사용자 아이디
     */
    public String getLoginIdFromToken (String token)
    {
        Claims claims = getClaimsFormToken(token);
        return claims.get(SwinglabConst.LOGIN_ID).toString();
    }

    /**
     * 해당 토큰 만료값 구하기
     *
     * @param token : 토큰
     */
    public Long getExpirationFromToken (String token)
    {
        Claims claims = getClaimsFormToken(token);
        return claims.getExpiration().getTime() - new Date().getTime();
    }

    /**
     * 유효한 토큰인지 확인 해주는 메서드
     *
     * @param token
     * @return
     */
    public SwinglabConst.JWT_EXCEPT isValidToken (String token)
    {
        try
        {
            Claims claims = getClaimsFormToken(token);

            log.info("expireTime :" + claims.getExpiration());
            log.info("userId :" + claims.get(SwinglabConst.USER_ID));
            log.info("loginId :" + claims.get(SwinglabConst.LOGIN_ID));
            log.info("userNm :" + claims.get(SwinglabConst.USER_NM));

            return null;
        } catch (ExpiredJwtException exception)
        {
            log.error("Token Expired");
            return SwinglabConst.JWT_EXCEPT.EXPIRED;
        } catch (JwtException exception)
        {
            log.error("Token Tampered");
            return SwinglabConst.JWT_EXCEPT.TAMPERED;
        } catch (NullPointerException exception)
        {
            log.error("Token is null");
            return SwinglabConst.JWT_EXCEPT.NULL;
        }
    }

    /**
     * @param loginId
     * @return
     */
    public Authentication getAuthenticationFromToken (String loginId)
    {
        UserDetails userDetails = swinglabUserDetailsService.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}

