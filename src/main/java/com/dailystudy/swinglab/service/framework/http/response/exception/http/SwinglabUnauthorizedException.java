package com.dailystudy.swinglab.service.framework.http.response.exception.http;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.MessageProvider;

import java.util.List;

public class SwinglabUnauthorizedException extends SwinglabHttpException
{

	private static final long serialVersionUID = 1304197950075750813L;
	private final String title = "Unauthenticated User";
	
	public SwinglabUnauthorizedException()
    {
        super(PlatformHttpStatus.UNAUTHORIZED);
        setTitle(title);
    }

    public SwinglabUnauthorizedException(String errorCode, MessageProvider mp, String... keyParams)
    {
        super(PlatformHttpStatus.UNAUTHORIZED, errorCode, mp, keyParams);
        setTitle(title);
    }

    public SwinglabUnauthorizedException(String message, List<String> details)
    {
        super(PlatformHttpStatus.UNAUTHORIZED, message, details);
        setTitle(title);
    }

    public SwinglabUnauthorizedException(String title, String message)
    {
        super(PlatformHttpStatus.UNAUTHORIZED, message);
        setTitle(title);
    }

    public SwinglabUnauthorizedException(String message)
    {
        super(PlatformHttpStatus.UNAUTHORIZED, message);
        setTitle(title);
    }

    public SwinglabUnauthorizedException(Throwable cause)
    {
        super(PlatformHttpStatus.UNAUTHORIZED, cause);
        setTitle(title);
    }

    public SwinglabUnauthorizedException(Throwable cause, List<String> details)
    {
        super(PlatformHttpStatus.UNAUTHORIZED, cause, details);
        setTitle(title);
    }

    public SwinglabUnauthorizedException(String message, Throwable cause)
    {
        super(PlatformHttpStatus.UNAUTHORIZED, message, cause);
        setTitle(title);
    }
}
