package com.dailystudy.swinglab.service.framework.http.response.exception;

/**
 *
 * Macrogen framework에서 사용할 최상위 RuntimeException 입니다.
 *
 */
public class SwinglabRuntimeException extends RuntimeException
{
	private static final long serialVersionUID = -3176982830265998007L;

	/**
     * @see RuntimeException#RuntimeException()
     */
    public SwinglabRuntimeException() {
        super();
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public SwinglabRuntimeException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable) 
     */
    public SwinglabRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable) 
     */
    public SwinglabRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    protected SwinglabRuntimeException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
