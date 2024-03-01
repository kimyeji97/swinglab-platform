package com.dailystudy.swinglab.service.framework.http.response.exception;

/**
 * Macrogen framework에서 사용할 최상위 명시적 Exception 입니다.
 */
public class SwinglabException extends Exception
{
    /**
     * @see Exception#Exception()
     */
    public SwinglabException() {
        super();
    }

    /**
     * @see Exception#Exception(String)
     */
    public SwinglabException(String message) {
        super(message);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public SwinglabException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see Exception#Exception(Throwable)
     */
    public SwinglabException(Throwable cause) {
        super(cause);
    }

    /**
     * @see Exception#Exception(String, Throwable, boolean, boolean)
     */
    protected SwinglabException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
