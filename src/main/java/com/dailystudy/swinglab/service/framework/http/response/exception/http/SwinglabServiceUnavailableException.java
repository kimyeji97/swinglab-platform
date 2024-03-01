package com.dailystudy.swinglab.service.framework.http.response.exception.http;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.MessageProvider;

import java.util.List;

public class SwinglabServiceUnavailableException extends SwinglabHttpException {

	private static final long serialVersionUID = -2868711210331007198L;

	public SwinglabServiceUnavailableException()
    {
        super(PlatformHttpStatus.SERVICE_UNAVAILABLE);
    }

    public SwinglabServiceUnavailableException(String errorCode, MessageProvider mp, String... keyParams)
    {
        super(PlatformHttpStatus.SERVICE_UNAVAILABLE, errorCode, mp, keyParams);
    }

    public SwinglabServiceUnavailableException(String message, List<String> details)
    {
        super(PlatformHttpStatus.SERVICE_UNAVAILABLE, message, details);
    }

    public SwinglabServiceUnavailableException(String message)
    {
        super(PlatformHttpStatus.SERVICE_UNAVAILABLE, message);
    }

    public SwinglabServiceUnavailableException(Throwable cause)
    {
        super(PlatformHttpStatus.SERVICE_UNAVAILABLE, cause);
    }

    public SwinglabServiceUnavailableException(Throwable cause, List<String> details)
    {
        super(PlatformHttpStatus.SERVICE_UNAVAILABLE, cause, details);
    }

    public SwinglabServiceUnavailableException(String message, Throwable cause)
    {
        super(PlatformHttpStatus.SERVICE_UNAVAILABLE, message, cause);
    }

}
