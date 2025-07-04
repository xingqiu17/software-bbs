package com.quark.porent.service.impl;

import com.quark.porent.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void send(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【校园论坛】您的验证码");
        message.setText("您好，您的验证码是：" + code + "，有效期5分钟，请勿泄露！");
        message.setTo(to);
        message.setFrom("1257988237@qq.com");  // 必须与spring.mail.username一致
        mailSender.send(message);

        System.out.println("已向 " + to + " 发送验证码邮件");
    }
}
