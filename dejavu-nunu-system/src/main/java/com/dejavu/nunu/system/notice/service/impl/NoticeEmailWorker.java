package com.dejavu.nunu.system.notice.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.core.utils.EmailUtil;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.tenant.entity.TenantEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoticeEmailWorker implements Runnable {


    private TenantEntity tenantEntity;

    private PaymentEntity paymentEntity;

    private NoticeProperties noticeProperties;

    public NoticeEmailWorker(TenantEntity tenantEntity, PaymentEntity paymentEntity, NoticeProperties noticeProperties) {
        this.tenantEntity = tenantEntity;
        this.paymentEntity = paymentEntity;
        this.noticeProperties = noticeProperties;
    }

    @Override
    public void run() {
        try {
            this.sendEmail();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendEmail() throws Exception {

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
        String result = template.render(Dict.create().set("tenantId", tenantEntity.getId()).set("tenantName", tenantEntity.getName()));

        EmailUtil.sendHtml(mailAccount, toEmailAddress, title, result);


    }


}
