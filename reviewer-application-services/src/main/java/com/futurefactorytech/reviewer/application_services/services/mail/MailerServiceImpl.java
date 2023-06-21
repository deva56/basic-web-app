package com.futurefactorytech.reviewer.application_services.services.mail;

import com.futurefactorytech.reviewer.api.services.mail.MailerService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import java.nio.charset.StandardCharsets;

@Named
public class MailerServiceImpl implements MailerService {

    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(MailerService.class);
    private final MailContentBuilder mailContentBuilder;

    @Value("${application-config.email.from-address}")
    private String FROM_EMAIL;

    @Value("${application-config.client-application-url}")
    private String CLIENT_APPLICATION_URL;

    @Inject
    public MailerServiceImpl(JavaMailSender javaMailSender, MailContentBuilder mailContentBuilder) {
        this.javaMailSender = javaMailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    @Override
    @Async
    public void sendEmail(String to, String from, String subject,
                          String content, boolean isHtml, boolean isMultipart) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            logger.debug("Successfully sent email to address: " + to);

        } catch (MailException | MessagingException e) {
            logger.error("Error while sending email to address: " + to, e);
            throw new RuntimeException(e);
        }
    }

    @Async
    private void sendEmailFromTemplate(String subject, String emailTo, String templateName, String message) {
        String content = mailContentBuilder.buildSimpleTemplateMessage(templateName, message);
        sendEmail(emailTo, FROM_EMAIL, subject, content, true, false);
    }

    @Async
    @Override
    public void sendActivationEmail(String emailTo, String activationCode) {
        String message = String.format("%s/activate-account/%s", CLIENT_APPLICATION_URL, activationCode);
        sendEmailFromTemplate("Account verification needed", emailTo, "activationEmail", message);
    }

    @Async
    @Override
    public void sendForgotPasswordEmail(String emailTo, String forgotPasswordCode) {
        String message = String.format("%s/forgot-password/%s", CLIENT_APPLICATION_URL, forgotPasswordCode);
        sendEmailFromTemplate("Password reset requested", emailTo, "forgotPasswordEmail", message);
    }
}
