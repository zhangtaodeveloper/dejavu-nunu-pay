package com.dejavu.nunu.system.notice.exception;

import com.dejavu.nunu.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum NoticeException implements ErrorType {


    PAYMENT_NOT_FOUNT("101001", "请检查支付信息"),
    SEND_EMAIL_ADDRESS("101002", "租户未配置通知邮件地址");

    /**
     * 错误类型码
     */
    private String code;

    /**
     * 错误类型描述信息
     */
    private String msg;

    NoticeException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
