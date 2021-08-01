package com.dejavu.nunu.system.tenant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tenant")
public class TenantEntity {


    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private String name;

    private String apiKey;

    private Date createdTime;

    private Date updatedTime;

}
