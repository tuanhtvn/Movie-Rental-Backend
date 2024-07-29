package com.rental.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

import java.time.ZonedDateTime;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendMailVerify(String toEmail, String userName, String verifyCode) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setTo(toEmail);
                messageHelper.setSubject("Rental movie - Mã xác minh");

                Context context = new Context();
                context.setVariable("UserName", userName);
                context.setVariable("ToEmail", toEmail);
                context.setVariable("VerifyCode", verifyCode);

                String content = templateEngine.process("VerifyTemplate", context);

                messageHelper.setText(content, true);
            }
        };
        mailSender.send(preparator);
    }

    @Override
    public void notifyAutoRenewal(String toEmail, String userName,
                                  String packageName, ZonedDateTime expirationDate, long minutesLeft) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setTo(toEmail);
                messageHelper.setSubject("Rental movie - Thông báo đã gia hạn gói thuê tự động");

                Context context = new Context();
                context.setVariable("userName", userName);
                context.setVariable("packageName", packageName);
                context.setVariable("expirationDate", expirationDate);
                context.setVariable("minutesLeft", minutesLeft);

                String content = templateEngine.process("AutoRenewalTemplate", context);

                messageHelper.setText(content, true);
            }
        };
        mailSender.send(preparator);
    }


}
