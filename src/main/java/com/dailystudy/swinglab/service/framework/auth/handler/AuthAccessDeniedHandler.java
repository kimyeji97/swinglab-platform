package com.dailystudy.swinglab.service.framework.auth.handler;

import com.dailystudy.swinglab.service.framework.http.response.domain.ErrorResponse;
import com.dailystudy.swinglab.service.framework.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class AuthAccessDeniedHandler implements AccessDeniedHandler
{
    @Override
    public void handle (HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException
    {
        try
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(String.valueOf(HttpStatus.FORBIDDEN.value()));
            errorResponse.setErrorMessage("접근할 수 있는 권한이 없습니다.");

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtil.parseJsonObject(errorResponse));
        } catch (Exception e)
        {
            log.error(" {} : {} : e", request.getRequestURI(), e.getMessage(), e);
        }
    }
}
