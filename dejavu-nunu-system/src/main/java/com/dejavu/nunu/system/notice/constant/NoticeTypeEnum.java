package com.dejavu.nunu.system.notice.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 通知类型
 */
public enum NoticeTypeEnum {


    EMAIL("EMAIL", "邮件"),
    SMS("SMS", "短信");

    NoticeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final String code;

    private final String desc;

}
