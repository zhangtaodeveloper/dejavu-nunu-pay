package com.dejavu.nunu.system.global.exception;

/**
 * 重复异常
 */
public class GlobalLockRepException extends Exception{

    public GlobalLockRepException() {
        super();
    }

    public GlobalLockRepException(String message) {
        super(message);
    }

    public GlobalLockRepException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalLockRepException(Throwable cause) {
        super(cause);
    }

    protected GlobalLockRepException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
