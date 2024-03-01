package com.dailystudy.swinglab.service.framework.auth.filter;

import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.http.response.domain.ErrorResponse;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabHttpException;
import com.dailystudy.swinglab.service.framework.utils.JsonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthExceptionFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            filterChain.doFilter(request, response);
        } catch (SwinglabHttpException e)
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(String.valueOf(e.getStatusCode().value()));
            errorResponse.setTitle(e.getTitle());
            errorResponse.setErrorMessage(e.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding(SwinglabConst.UTF8);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtil.parseJsonObject(errorResponse));
        }
    }
}
