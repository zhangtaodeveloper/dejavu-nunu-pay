package com.dejavu.nunu.system.notice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.core.exception.BaseException;
import com.dejavu.nunu.system.notice.entity.NoticeEntity;
import com.dejavu.nunu.system.notice.exception.NoticeException;
import com.dejavu.nunu.system.notice.mapper.NoticeMapper;
import com.dejavu.nunu.system.notice.service.NoticeService;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.payment.service.PaymentService;
import com.dejavu.nunu.system.tenant.entity.TenantEntity;
import com.dejavu.nunu.system.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private NoticeProperties noticeProperties;


    private Executor executor = Executors.newFixedThreadPool(10);

    @Override
    public void send(String orderNo) {
        //获取支付记录
        PaymentEntity paymentEntity = paymentService.getByOrderNo(orderNo);

        if (null == paymentEntity) {
            throw new BaseException(NoticeException.PAYMENT_NOT_FOUNT);
        }

        //获取通知地址
        TenantEntity tenantEntity = tenantService.getByTenantId(paymentEntity.getTenantId());

        if (StrUtil.isBlank(tenantEntity.getEmail())) {
            throw new BaseException(NoticeException.SEND_EMAIL_ADDRESS);
        }

        //邮件通知
        NoticeEmailWorker noticeEmailWorker = new NoticeEmailWorker(tenantEntity, paymentEntity, noticeProperties);
        executor.execute(noticeEmailWorker);

    }


}
