package com.dejavu.nunu.system;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.dejavu.nunu.config.NoticeProperties;
import com.dejavu.nunu.core.utils.EmailUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NoticePropertiesTest extends TestCore {


    @Autowired
    private NoticeProperties noticeProperties;

    @Test
    public void getValueTest() {
        System.out.println(noticeProperties.getEmail().getUser());
    }


    @Test
    public void sendTest() throws Exception {

        NoticeProperties.Email email = noticeProperties.getEmail();

        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(email.getHost());
        mailAccount.setPort(email.getPort());
        mailAccount.setAuth(email.getAuth());
        mailAccount.setUser(email.getUser());
        mailAccount.setPass(email.getPass());
        mailAccount.setFrom(email.getFrom());

        EmailUtil.send(mailAccount, "zhangtaodeveloper@163.com", "测试标题", "测试内容");

    }

    @Test
    public void sendHtmlTest() throws Exception {

        TemplateConfig templateConfig = new TemplateConfig("/template", TemplateConfig.ResourceMode.CLASSPATH);
        templateConfig.setCustomEngine(FreemarkerEngine.class);

        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
        Template template = engine.getTemplate("EmailTemplate.html");
        String result = template.render(Dict.create().set("tenantId", "666666"));


        NoticeProperties.Email email = noticeProperties.getEmail();

        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(email.getHost());
        mailAccount.setPort(email.getPort());
        mailAccount.setAuth(email.getAuth());
        mailAccount.setUser(email.getUser());
        mailAccount.setPass(email.getPass());
        mailAccount.setFrom(email.getFrom());


        EmailUtil.sendHtml(mailAccount, "zhangtaodeveloper@163.com", "测试标题", result);

    }


}
