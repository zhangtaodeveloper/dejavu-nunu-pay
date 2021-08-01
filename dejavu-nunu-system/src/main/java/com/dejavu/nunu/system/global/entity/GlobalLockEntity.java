package com.dejavu.nunu.system.global.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("global_lock")
public class GlobalLockEntity {

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private Long tenantId;

    @TableField("`key`")
    private String key;

    private String type;

    private Date createdTime;

}
