package com.lipeng.pay.common.exception;

public class CommonPayException extends RuntimeException {

    /**
     * Default constructor
     */
    public CommonPayException() {
        super();
    }

    /**
     * Constructor with message & cause
     */
    public CommonPayException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message
     */
    public CommonPayException(String message) {
        super(message);
    }

    /**
     * Constructor with message format
     */
    public CommonPayException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    /**
     * Constructor with cause
     */
    public CommonPayException(Throwable cause) {
        super(cause);
    }

}