package com.dailystudy.swinglab.service.framework.http.response.exception;

/**
 *
 * Macrogen framework에서 사용할 최상위 RuntimeException 입니다.
 *
 * @author Gwanggeun Yoo
 */
public class SwinglabInitializingFailedException extends SwinglabRuntimeException
{
    /**
     * @see RuntimeException#RuntimeException()
     */
    public SwinglabInitializingFailedException() {
        super();
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public SwinglabInitializingFailedException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public SwinglabInitializingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public SwinglabInitializingFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    protected SwinglabInitializingFailedException(String message, Throwable cause,
                                                  boolean enableSuppression,
                                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
