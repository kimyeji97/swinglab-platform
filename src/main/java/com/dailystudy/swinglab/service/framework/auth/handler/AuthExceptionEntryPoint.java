package com.dailystudy.swinglab.service.framework.auth.handler;

import com.dailystudy.swinglab.service.framework.http.response.domain.ErrorResponse;
import com.dailystudy.swinglab.service.framework.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 *
 */
@Slf4j
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint
{
    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException
    {
        try
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
            errorResponse.setErrorMessage("인증되지 않은 사용자 입니다.");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtil.parseJsonObject(errorResponse));
        } catch (Exception e)
        {
            log.error(" {} : {} : e", request.getRequestURI(), e.getMessage(), e);
        }
    }
}
