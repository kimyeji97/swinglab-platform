package com.dailystudy.swinglab.service.framework.http.request;

import com.dailystudy.swinglab.service.framework.http.request.domain.ApiRequestCache;
import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.domain.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestThreadLocal
{
    private final static ThreadLocal<ApiRequestCache> restApiResponseThreadLocal = ThreadLocal.withInitial(ApiRequestCache::new);

    public static ApiRequestCache getRestApiResponse ()
    {
        return restApiResponseThreadLocal.get();
    }

    public static void setResponseBody (String responseBody)
    {
        ApiRequestCache restApiResponse = restApiResponseThreadLocal.get();
        if (restApiResponse == null)
        {
            restApiResponse = new ApiRequestCache();
            restApiResponseThreadLocal.set(restApiResponse);
        }

        restApiResponse.setHttpSttus(PlatformHttpStatus.OK);
        restApiResponse.setResponseBody(responseBody);
    }

    public static void setRestApiResponse (PlatformHttpStatus httpSttus)
    {
        setRestApiResponse(httpSttus, null);
    }

    public static void setRestApiResponse (PlatformHttpStatus httpSttus, ErrorResponse errorResponse)
    {
        ApiRequestCache restApiResponse = restApiResponseThreadLocal.get();
        if (restApiResponse == null)
        {
            restApiResponse = new ApiRequestCache();
            restApiResponseThreadLocal.set(restApiResponse);
        }

        restApiResponse.setHttpSttus(httpSttus);
        restApiResponse.setErrorResponse(errorResponse);
    }

    public static void setRequestBody (String requestBody)
    {
        ApiRequestCache restApiResponse = restApiResponseThreadLocal.get();
        if (restApiResponse == null)
        {
            restApiResponse = new ApiRequestCache();
            restApiResponseThreadLocal.set(restApiResponse);
        }
        restApiResponse.setRequestBody(requestBody);
    }

    public static void remove ()
    {
        restApiResponseThreadLocal.remove();
    }
}
