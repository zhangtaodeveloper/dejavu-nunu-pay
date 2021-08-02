package com.dejavu.nunu.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "notice")
public class NoticeProperties {


    private String noticeUrl;

    private Email email;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Email {

        private String host;

        private int port;

        private Boolean auth;

        private String from;

        private String user;

        private String pass;

    }
}
