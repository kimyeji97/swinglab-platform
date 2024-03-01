package com.dailystudy.swinglab.service.framework.http.request.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PlatformCorsFilter implements Filter
{

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
//        if (StringUtils.equals(request.getContextPath(), "/admin"))
//        {
//            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
//        } else
//        {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
//        response.setHeader("Access-Control-Allow-Headers",
//            "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization, Access-Control-Request-Headers");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*, Authorization");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*, Authorization");
        
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()))
        {
            response.setStatus(HttpServletResponse.SC_OK);
        } else
        {
            chain.doFilter(req, res);
        }
    }
}
