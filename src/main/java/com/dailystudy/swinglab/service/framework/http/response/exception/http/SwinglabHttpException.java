package com.dailystudy.swinglab.service.framework.http.response.exception.http;

import com.dailystudy.swinglab.service.framework.http.response.PlatformHttpStatus;
import com.dailystudy.swinglab.service.framework.http.response.MessageProvider;
import com.dailystudy.swinglab.service.framework.http.response.exception.SwinglabRuntimeException;
import com.dailystudy.swinglab.service.framework.utils.ArrayUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class SwinglabHttpException extends SwinglabRuntimeException {

    private static final long serialVersionUID = -5581759208454112995L;

    private final PlatformHttpStatus statusCode;
    private String errorCode;
    private String title;
    private final String message;
    private final String[] details;

    public SwinglabHttpException(PlatformHttpStatus statusCode)
    {
        this(statusCode, statusCode.getReasonPhrase());
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, String errorCode, String message, Throwable cause,
                                 List<String> details)
    {
        super(message, cause);

        this.statusCode = statusCode;
        this.message = message;
        this.details = details != null ? ArrayUtil.toStringArray(details) : null;
        this.errorCode = errorCode;
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, String errorCode, MessageProvider mp, String... keyParams)
    {
        this(statusCode, errorCode, mp.getMessage(errorCode, keyParams), null, null);
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, String message, List<String> details)
    {
        this(statusCode, null, message, null, details);
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, String errorCode, String message, List<String> details)
    {
        this(statusCode, errorCode, message, null, details);
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, String message)
    {
        this(statusCode, (String) null, message, null, null);
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, String errorCode, String message)
    {
        this(statusCode, errorCode, message, null, null);
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, Throwable cause)
    {
        this(statusCode, null, null, cause, null);
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, Throwable cause, List<String> details)
    {
        this(statusCode, null, statusCode.getReasonPhrase(), cause, details);
    }

    public SwinglabHttpException(PlatformHttpStatus statusCode, String message, Throwable cause)
    {
        this(statusCode, null, message, cause, null);
    }

    public String[] getDetails()
    {
        if (this.details == null)
        {
            return null;
        }
        return Arrays.copyOf(details, details.length);
    }
}
