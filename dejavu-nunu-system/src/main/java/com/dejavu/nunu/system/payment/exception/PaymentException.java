package com.dejavu.nunu.system.payment.exception;

import com.dejavu.nunu.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum PaymentException implements ErrorType {

    TENANT_ERROR("100001", "请检查租户信息！");

    /**
     * 错误类型码
     */
    private String code;

    /**
     * 错误类型描述信息
     */
    private String msg;

    PaymentException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
