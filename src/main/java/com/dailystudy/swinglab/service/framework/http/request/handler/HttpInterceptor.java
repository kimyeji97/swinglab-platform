package com.dailystudy.swinglab.service.framework.http.request.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component("platformHttpInterceptor")
public class HttpInterceptor implements HandlerInterceptor  {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		boolean result = true;
		if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) { 
			result = true; 
		}

		if (response != null && response.getContentType() != null && response.getContentType().toLowerCase().startsWith("application/json")) { 
			result = true; 
		}
		return result;
	}
}
