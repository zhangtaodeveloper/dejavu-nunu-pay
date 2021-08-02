package com.dejavu.nunu.system.notice.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 通知状态
 */
public enum NoticeStatusEnum {


    WAIT_SEND("WAIT_SEND", "待发送"),

    SUCCESS_SEND("SUCCESS_SEND", "成功发送");


    NoticeStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final String code;

    private final String desc;

}
