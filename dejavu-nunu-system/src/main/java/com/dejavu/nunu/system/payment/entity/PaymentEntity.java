package com.dejavu.nunu.system.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dejavu.nunu.system.payment.constant.PaymentStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("payment")
public class PaymentEntity {

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private Long tenantId;

    private String orderNo;

    private BigDecimal amount;

    private PaymentStatusEnum status;

    private Date createdTime;

    private Date updatedTime;


}
