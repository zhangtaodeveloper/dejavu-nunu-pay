package com.dejavu.nunu.core.exception;

import lombok.Getter;

/**
 * 基本异常.
 *
 * @author zt
 * @date 2020/11/04 15:57.
 */
@Getter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -2088891163114521095L;

    /**
     * 异常对应的错误类型
     */
    private final ErrorType errorType;

    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public BaseException(ErrorType errorType) {
        super(errorType.getMsg());
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

}
