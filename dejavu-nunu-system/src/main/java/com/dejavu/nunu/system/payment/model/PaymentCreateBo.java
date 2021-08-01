package com.dejavu.nunu.system.payment.model;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateBo {


    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal amount;

}
