package com.dejavu.nunu.system.notice.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.core.exception.BaseException;
import com.dejavu.nunu.core.utils.SecurityUtil;
import com.dejavu.nunu.system.notice.constant.NoticeStatusEnum;
import com.dejavu.nunu.system.notice.constant.NoticeTypeEnum;
import com.dejavu.nunu.system.notice.entity.NoticeEntity;
import com.dejavu.nunu.system.notice.exception.NoticeException;
import com.dejavu.nunu.system.notice.mapper.NoticeMapper;
import com.dejavu.nunu.system.notice.model.NoticeNotifyDataBo;
import com.dejavu.nunu.system.notice.service.NoticeService;
import com.dejavu.nunu.system.payment.constant.PaymentStatusEnum;
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
        long noticeId = new Sequence().nextId();

        String emailContext = getEmailContext(noticeId, paymentEntity, tenantEntity);

        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setId(noticeId);
        noticeEntity.setTenantId(tenantEntity.getId());
        noticeEntity.setOrderNo(orderNo);
        noticeEntity.setContext(emailContext);
        noticeEntity.setStatus(NoticeStatusEnum.WAIT_SEND);
        noticeEntity.setType(NoticeTypeEnum.EMAIL);
        noticeEntity.setCreatedTime(new Date());
        noticeEntity.setUpdatedTime(new Date());

        baseMapper.insert(noticeEntity);

        NoticeEmailContext noticeEmailContext = new NoticeEmailContext();
        noticeEmailContext.setNoticeService(this);
        noticeEmailContext.setTenantEntity(tenantEntity);
        noticeEmailContext.setPaymentEntity(paymentEntity);
        noticeEmailContext.setNoticeProperties(noticeProperties);
        noticeEmailContext.setNoticeId(noticeId);
        noticeEmailContext.setContext(emailContext);
        NoticeEmailWorker noticeEmailWorker = new NoticeEmailWorker(noticeEmailContext);
        executor.execute(noticeEmailWorker);

    }


    @Override
    public void updateStatus(Long noticeId, NoticeStatusEnum status) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setStatus(status);
        noticeEntity.setUpdatedTime(new Date());
        UpdateWrapper<NoticeEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(NoticeEntity::getId, noticeId);
        baseMapper.update(noticeEntity, updateWrapper);
    }

    @Override
    public void confirm(Long tenantId, String data) {
        //租户信息
        TenantEntity tenantEntity = tenantService.getByTenantId(tenantId);
        //解密
        String jsonContext = SecurityUtil.decrypt(tenantEntity.getApiKey(), data);
        NoticeNotifyDataBo noticeNotifyDataBo = JSONUtil.toBean(jsonContext, NoticeNotifyDataBo.class);
        //更新通知记录
        updateStatus(noticeNotifyDataBo.getNoticeId(), NoticeStatusEnum.SUCCESS_CONFIRM);
        //更新支付记录
        paymentService.updateStatus(tenantId, noticeNotifyDataBo.getOrderNo(), PaymentStatusEnum.SUCCESS_PAY);
        //TODO 回调接入系统

    }


    private String getEmailContext(long noticeId, PaymentEntity paymentEntity, TenantEntity tenantEntity) {
        TemplateConfig templateConfig = new TemplateConfig("/template", TemplateConfig.ResourceMode.CLASSPATH);
        templateConfig.setCustomEngine(FreemarkerEngine.class);

        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
        Template template = engine.getTemplate("EmailTemplate.html");


        NoticeNotifyDataBo noticeNotifyDataBo = new NoticeNotifyDataBo();
        noticeNotifyDataBo.setNoticeId(noticeId);
        noticeNotifyDataBo.setOrderNo(paymentEntity.getOrderNo());

        String data = SecurityUtil.encrypt(tenantEntity.getApiKey(), noticeNotifyDataBo);

        String noticeUrl = noticeProperties.getNoticeUrl() + tenantEntity.getId() + "/" + data;

        Dict dict = Dict.create()
                .set("tenantId", tenantEntity.getId())
                .set("tenantName", tenantEntity.getName())
                .set("noticeUrl", noticeUrl)
                .set("orderNo", paymentEntity.getOrderNo())
                .set("date", DateUtil.now());

        return template.render(dict);
    }


}
