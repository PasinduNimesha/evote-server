package com.evote.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String fromEmail;

    @Value("${email.from-name}")
    private String fromName;

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your OTP Code - E-Vote");
            message.setText("Your OTP code is: " + otp + "\n\nThis OTP will expire in 5 minutes.");

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", toEmail, e);
        }
    }

    @Async
    public void sendLoginConfirmation(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Login Confirmation - E-Vote");
            message.setText("You have successfully logged in to E-Vote.\n\n" +
                    "If this was not you, please contact us immediately.");

            mailSender.send(message);
            log.info("Login confirmation email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send login confirmation email to: {}", toEmail, e);
        }
    }

    @Async
    public void sendVerificationCodeEmail(String toEmail, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Vote Verification Code - E-Vote");
            message.setText("Your vote verification code is:\n\n" + verificationCode +
                    "\n\nThis code can be used to verify your vote submission.");

            mailSender.send(message);
            log.info("Verification code email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification code email to: {}", toEmail, e);
        }
    }
}

