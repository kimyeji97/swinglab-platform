package com.dailystudy.swinglab.service.framework.http.response.exception.http;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.MessageProvider;

import java.util.List;

public class SwinglabNotFoundException extends SwinglabHttpException {

	private static final long serialVersionUID = 8838556050207962291L;

	public SwinglabNotFoundException()
    {
        super(PlatformHttpStatus.NOT_FOUND);
    }

    public SwinglabNotFoundException(String errorCode, MessageProvider mp, String... keyParams)
    {
        super(PlatformHttpStatus.NOT_FOUND, errorCode, mp, keyParams);
    }

    public SwinglabNotFoundException(String message, List<String> details)
    {
        super(PlatformHttpStatus.NOT_FOUND, message, details);
    }

    public SwinglabNotFoundException(String message)
    {
        super(PlatformHttpStatus.NOT_FOUND, message);
    }

    public SwinglabNotFoundException(Throwable cause)
    {
        super(PlatformHttpStatus.NOT_FOUND, cause);
    }

    public SwinglabNotFoundException(Throwable cause, List<String> details)
    {
        super(PlatformHttpStatus.NOT_FOUND, cause, details);
    }

    public SwinglabNotFoundException(String message, Throwable cause)
    {
        super(PlatformHttpStatus.NOT_FOUND, message, cause);
    }

}
