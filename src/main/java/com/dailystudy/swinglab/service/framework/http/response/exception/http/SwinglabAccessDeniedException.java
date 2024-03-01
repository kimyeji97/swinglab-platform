package com.dailystudy.swinglab.service.framework.http.response.exception.http;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.MessageProvider;

import java.util.List;

public class SwinglabAccessDeniedException extends SwinglabHttpException {

	private static final long serialVersionUID = 6991196264206421622L;
	private static final String title = "Access is denied";

	public SwinglabAccessDeniedException()
    {
        super(PlatformHttpStatus.FORBIDDEN);
        setTitle(title);
    }

    public SwinglabAccessDeniedException(String errorCode, MessageProvider mp, String... keyParams)
    {
        super(PlatformHttpStatus.FORBIDDEN, errorCode, mp, keyParams);
        setTitle(title);
    }

    public SwinglabAccessDeniedException(String message, List<String> details)
    {
        super(PlatformHttpStatus.FORBIDDEN, message, details);
        setTitle(title);
    }

    public SwinglabAccessDeniedException(String message)
    {
        super(PlatformHttpStatus.FORBIDDEN, message);
        setTitle(title);
    }

    public SwinglabAccessDeniedException(Throwable cause)
    {
        super(PlatformHttpStatus.FORBIDDEN, cause);
        setTitle(title);
    }

    public SwinglabAccessDeniedException(Throwable cause, List<String> details)
    {
        super(PlatformHttpStatus.FORBIDDEN, cause, details);
        setTitle(title);
    }

    public SwinglabAccessDeniedException(String message, Throwable cause)
    {
        super(PlatformHttpStatus.FORBIDDEN, message, cause);
        setTitle(title);
    }

}
