package com.futurefactorytech.reviewer.api.services.mail;

public interface MailerService {

    void sendEmail(String to, String from, String subject,
                                      String content, boolean isHtml, boolean isMultipart);

    void sendActivationEmail(String emailTo, String activationCode);

    void sendForgotPasswordEmail(String emailTo, String forgotPasswordCode);
}
