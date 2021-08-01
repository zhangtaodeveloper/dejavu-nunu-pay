package com.dejavu.nunu.system.payment.model;

import lombok.Data;

@Data
public class PaymentBaseBo {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 加密内容
     */
    private String context;


}
