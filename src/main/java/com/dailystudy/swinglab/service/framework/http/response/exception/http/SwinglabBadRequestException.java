package com.dailystudy.swinglab.service.framework.http.response.exception.http;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.MessageProvider;

import java.util.List;

/**
 * @author Gwanggeun Yoo
 */
public class SwinglabBadRequestException extends SwinglabHttpException
{
    private static final long serialVersionUID = -2235869320525832134L;

    public SwinglabBadRequestException()
    {
        super(PlatformHttpStatus.BAD_REQUEST);
    }

    public SwinglabBadRequestException(String errorCode, MessageProvider mp, String... keyParams)
    {
        super(PlatformHttpStatus.BAD_REQUEST, errorCode, mp, keyParams);
    }

    public SwinglabBadRequestException(String message, List<String> details)
    {
        super(PlatformHttpStatus.BAD_REQUEST, message, details);
    }

    public SwinglabBadRequestException(String message)
    {
        super(PlatformHttpStatus.BAD_REQUEST, message);
    }

    public SwinglabBadRequestException(Throwable cause)
    {
        super(PlatformHttpStatus.BAD_REQUEST, cause);
    }

    public SwinglabBadRequestException(Throwable cause, List<String> details)
    {
        super(PlatformHttpStatus.BAD_REQUEST, cause, details);
    }

    public SwinglabBadRequestException(String message, Throwable cause)
    {
        super(PlatformHttpStatus.BAD_REQUEST, message, cause);
    }
}
