package com.dailystudy.swinglab.service.framework.http.response.exception;

/**
 *
 * Macrogen 메소드를 직접 호출하면 안되는 상황, 또는 구현되지 않았을 때 사용한다.
 *
 * @author Gwanggeun Yoo
 */
public class SwinglabNotSupportedMethodException extends SwinglabRuntimeException
{
    /**
     * @see RuntimeException#RuntimeException()
     */
    public SwinglabNotSupportedMethodException() {
        super();
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public SwinglabNotSupportedMethodException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public SwinglabNotSupportedMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public SwinglabNotSupportedMethodException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    protected SwinglabNotSupportedMethodException(String message, Throwable cause,
                                                  boolean enableSuppression,
                                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
