package com.dejavu.nunu.system.notice.service.impl;

import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.system.notice.service.NoticeService;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.tenant.entity.TenantEntity;
import lombok.Data;

@Data
public class NoticeEmailContext {


    private NoticeService noticeService;

    private TenantEntity tenantEntity;

    private PaymentEntity paymentEntity;

    private NoticeProperties noticeProperties;

    private String context;


}
