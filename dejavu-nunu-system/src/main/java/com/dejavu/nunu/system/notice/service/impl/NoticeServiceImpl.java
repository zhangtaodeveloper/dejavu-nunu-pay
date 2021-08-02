package com.dejavu.nunu.system.notice.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.core.utils.EmailUtil;
import com.dejavu.nunu.system.notice.entity.NoticeEntity;
import com.dejavu.nunu.system.notice.mapper.NoticeMapper;
import com.dejavu.nunu.system.notice.service.NoticeService;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.payment.service.PaymentService;
import com.dejavu.nunu.system.tenant.entity.TenantEntity;
import com.dejavu.nunu.system.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private NoticeProperties noticeProperties;

    @Override
    public void send(String orderNo) {
        //获取支付记录
        PaymentEntity paymentEntity = paymentService.getByOrderNo(orderNo);
        //获取通知地址
        TenantEntity tenantEntity = tenantService.getByTenantId(paymentEntity.getTenantId());
        try {
            //邮件通知
            this.sendEmail(tenantEntity, paymentEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendEmail(TenantEntity tenantEntity, PaymentEntity paymentEntity) throws Exception {
        NoticeProperties.Email email = noticeProperties.getEmail();

        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(email.getHost());
        mailAccount.setPort(email.getPort());
        mailAccount.setAuth(email.getAuth());
        mailAccount.setUser(email.getUser());
        mailAccount.setPass(email.getPass());
        mailAccount.setFrom(email.getFrom());

        String title = "确认收款通知-订单号：" + paymentEntity.getOrderNo();

        String toEmailAddress = tenantEntity.getEmail();

        TemplateConfig templateConfig = new TemplateConfig("/template", TemplateConfig.ResourceMode.CLASSPATH);
        templateConfig.setCustomEngine(FreemarkerEngine.class);

        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
        Template template = engine.getTemplate("EmailTemplate.html");
        String result = template.render(Dict.create().set("tenantId", "666666").set("tenantName", tenantEntity.getName()));

        EmailUtil.sendHtml(mailAccount, toEmailAddress, title, result);


    }


}
