package com.dejavu.nunu.system.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dejavu.nunu.system.notice.constant.NoticeStatusEnum;
import com.dejavu.nunu.system.notice.constant.NoticeTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notice")
public class NoticeEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long tenantId;

    private String orderNo;

    private String context;

    private NoticeStatusEnum status;

    private NoticeTypeEnum type;

    private Date createdTime;

    private Date updatedTime;


}
