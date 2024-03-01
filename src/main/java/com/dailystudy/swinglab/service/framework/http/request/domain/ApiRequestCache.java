package com.dailystudy.swinglab.service.framework.http.request.domain;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.domain.ErrorResponse;
import lombok.Data;

@Data
public class ApiRequestCache
{
	private PlatformHttpStatus httpSttus;
	private String requestBody;
	private String responseBody;
	private ErrorResponse errorResponse;
}
