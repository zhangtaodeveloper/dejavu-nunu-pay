package com.dejavu.nunu.system.notice.service.impl;

import cn.hutool.extra.mail.MailAccount;
import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.core.utils.EmailUtil;
import com.dejavu.nunu.system.notice.constant.NoticeStatusEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoticeEmailWorker implements Runnable {


    private final NoticeEmailContext noticeEmailContext;

    public NoticeEmailWorker(NoticeEmailContext noticeEmailContext) {
        this.noticeEmailContext = noticeEmailContext;
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

        NoticeProperties.Email email = noticeEmailContext.getNoticeProperties().getEmail();

        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(email.getHost());
        mailAccount.setPort(email.getPort());
        mailAccount.setAuth(email.getAuth());
        mailAccount.setUser(email.getUser());
        mailAccount.setPass(email.getPass());
        mailAccount.setFrom(email.getFrom());

        String title = "确认收款通知-订单号：" + noticeEmailContext.getPaymentEntity().getOrderNo();

        String toEmailAddress = noticeEmailContext.getTenantEntity().getEmail();

        EmailUtil.sendHtml(mailAccount, toEmailAddress, title, noticeEmailContext.getContext());

        noticeEmailContext.getNoticeService().updateStatus(noticeEmailContext.getNoticeId(), NoticeStatusEnum.SUCCESS_SEND);

    }


}
