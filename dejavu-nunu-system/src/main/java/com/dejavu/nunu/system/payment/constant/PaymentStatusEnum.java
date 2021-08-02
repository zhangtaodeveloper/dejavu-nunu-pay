package com.dejavu.nunu.system.payment.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 支付状态
 */
public enum PaymentStatusEnum {


    WAIT_PAY("WAIT_PAY", "待支付"),

    SUCCESS_PAY("SUCCESS_PAY", "支付成功");

    PaymentStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final String code;

    private final String desc;

}
