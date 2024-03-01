package com.dailystudy.swinglab.service.framework.http.response.exception.http;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.MessageProvider;

import java.util.List;

public class SwinglabInternalServerErrorException extends SwinglabHttpException {

	private static final long serialVersionUID = 6991196264206421622L;
	private static final String title = "Internal Server Error";

	public SwinglabInternalServerErrorException()
    {
        super(PlatformHttpStatus.INTERNAL_SERVER_ERROR);
        setTitle(title);
    }

    public SwinglabInternalServerErrorException(String errorCode, MessageProvider mp, String... keyParams)
    {
        super(PlatformHttpStatus.INTERNAL_SERVER_ERROR, errorCode, mp, keyParams);
        setTitle(title);
    }

    public SwinglabInternalServerErrorException(String message, List<String> details)
    {
        super(PlatformHttpStatus.INTERNAL_SERVER_ERROR, message, details);
        setTitle(title);
    }

    public SwinglabInternalServerErrorException(String message)
    {
        super(PlatformHttpStatus.INTERNAL_SERVER_ERROR, message);
        setTitle(title);
    }

    public SwinglabInternalServerErrorException(Throwable cause)
    {
        super(PlatformHttpStatus.INTERNAL_SERVER_ERROR, cause);
        setTitle(title);
    }

    public SwinglabInternalServerErrorException(Throwable cause, List<String> details)
    {
        super(PlatformHttpStatus.INTERNAL_SERVER_ERROR, cause, details);
        setTitle(title);
    }

    public SwinglabInternalServerErrorException(String message, Throwable cause)
    {
        super(PlatformHttpStatus.INTERNAL_SERVER_ERROR, message, cause);
        setTitle(title);
    }

}
