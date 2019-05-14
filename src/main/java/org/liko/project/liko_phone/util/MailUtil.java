package org.liko.project.liko_phone.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender jms;

    @Value("${spring.mail.username}")
    private String sender;

    @GetMapping("/send")
    public void send(String receiver, String subject, String context){
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom(sender);
        //接收者
        mainMessage.setTo(receiver);
        //发送的标题
        mainMessage.setSubject(subject);
        //发送的内容
        mainMessage.setText(context);
        jms.send(mainMessage);
    }
}
