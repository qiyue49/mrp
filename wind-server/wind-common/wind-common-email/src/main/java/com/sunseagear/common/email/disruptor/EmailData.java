package com.sunseagear.common.email.disruptor;

import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.io.Serializable;

public class EmailData implements Serializable {

    private MimeMessage message;
    @Getter
    private MailProperties mailProperties;

    public MimeMessage getMimeMessage() {
        return message;
    }

    public void setMimeMessage(MimeMessage message) {
        this.message = message;
    }

    public void setMailProperties(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }
}
