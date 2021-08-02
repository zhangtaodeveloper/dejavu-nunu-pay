package com.dejavu.nunu.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

public class EmailUtil {


    /**
     * 发送邮件通知
     *
     * @param account
     * @param email
     * @param title
     * @param context
     */
    public static void send(MailAccount account, String email, String title, String context) throws Exception {
        MailUtil.send(account, CollUtil.newArrayList(email), title, context, false);
    }


    /**
     * 发送邮件通知（HTML）
     *
     * @param account
     * @param email
     * @param title
     * @param htmlContext
     * @throws Exception
     */
    public static void sendHtml(MailAccount account, String email, String title, String htmlContext) throws Exception {
        MailUtil.send(account, CollUtil.newArrayList(email), title, htmlContext, true);
    }

}
