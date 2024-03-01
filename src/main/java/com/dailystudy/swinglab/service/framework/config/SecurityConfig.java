package com.dailystudy.swinglab.service.framework.config;

import com.dailystudy.swinglab.service.framework.auth.JwtTokenProvider;
import com.dailystudy.swinglab.service.framework.auth.filter.JwtAuthExceptionFilter;
import com.dailystudy.swinglab.service.framework.auth.filter.JwtAuthFilter;
import com.dailystudy.swinglab.service.framework.auth.handler.AuthAccessDeniedHandler;
import com.dailystudy.swinglab.service.framework.auth.handler.AuthExceptionEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    @Value("${security.ignoring}")
    private String[] ignoringUris;

    @Value("${security.permitAll}")
    private String[] permitAllUris;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder ()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthExceptionEntryPoint authExceptionEntryPoint ()
    {
        return new AuthExceptionEntryPoint();
    }

    @Bean
    public AuthAccessDeniedHandler authAccessDeniedHandler ()
    {
        return new AuthAccessDeniedHandler();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter ()
    {
        return new JwtAuthFilter(jwtTokenProvider,redisTemplate);
    }

    @Bean
    public JwtAuthExceptionFilter jwtExceptionFilter ()
    {
        return new JwtAuthExceptionFilter();
    }


    @Bean
    public HttpFirewall defaultHttpFirewall ()
    {
        return new DefaultHttpFirewall();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer ()
    {
        return (web) -> web.ignoring().requestMatchers(ignoringUris);
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception
    {
        // csrf, form 로그인 비활성화
        http.csrf().disable();
        http.formLogin().disable();

        // 권한 허용 uri 설정
        http.authorizeHttpRequests()
                .requestMatchers(permitAllUris).permitAll();

        // session 사용 비활성화
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 필터 추가 (JWT 토큰 인증 필터, JWT 토큰 인증 exception 필터)
        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter(), JwtAuthFilter.class);

        // 인증/인가 에러 핸들링
        http.exceptionHandling()
                .authenticationEntryPoint(authExceptionEntryPoint())
                .accessDeniedHandler(authAccessDeniedHandler());

        return http.build();
    }

}
