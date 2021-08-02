package com.dejavu.nunu.system.notice.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.core.exception.BaseException;
import com.dejavu.nunu.system.notice.constant.NoticeStatusEnum;
import com.dejavu.nunu.system.notice.constant.NoticeTypeEnum;
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

import java.util.Date;
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

        String emailContext = getEmailContext(tenantEntity);

        NoticeEmailContext noticeEmailContext = new NoticeEmailContext();
        noticeEmailContext.setNoticeService(this);
        noticeEmailContext.setTenantEntity(tenantEntity);
        noticeEmailContext.setPaymentEntity(paymentEntity);
        noticeEmailContext.setNoticeProperties(noticeProperties);
        noticeEmailContext.setContext(emailContext);

        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTenantId(tenantEntity.getId());
        noticeEntity.setOrderNo(orderNo);
        noticeEntity.setContext(emailContext);
        noticeEntity.setStatus(NoticeStatusEnum.WAIT_SEND);
        noticeEntity.setType(NoticeTypeEnum.EMAIL);
        noticeEntity.setCreatedTime(new Date());
        noticeEntity.setUpdatedTime(new Date());

        baseMapper.insert(noticeEntity);

        NoticeEmailWorker noticeEmailWorker = new NoticeEmailWorker(noticeEmailContext);
        executor.execute(noticeEmailWorker);

    }

    @Override
    public void updateStatus(Long tenantId, String orderNo, NoticeStatusEnum noticeStatusEnum) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setStatus(NoticeStatusEnum.SUCCESS_SEND);
        UpdateWrapper<NoticeEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(NoticeEntity::getTenantId, tenantId);
        updateWrapper.lambda().eq(NoticeEntity::getOrderNo, orderNo);
        baseMapper.update(noticeEntity, updateWrapper);
    }


    private String getEmailContext(TenantEntity tenantEntity) {
        TemplateConfig templateConfig = new TemplateConfig("/template", TemplateConfig.ResourceMode.CLASSPATH);
        templateConfig.setCustomEngine(FreemarkerEngine.class);

        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
        Template template = engine.getTemplate("EmailTemplate.html");

        return template.render(Dict.create().set("tenantId", tenantEntity.getId()).set("tenantName", tenantEntity.getName()));
    }


}
