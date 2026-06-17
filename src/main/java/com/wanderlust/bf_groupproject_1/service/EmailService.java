package com.wanderlust.bf_groupproject_1.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String to, String token, String baseUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Wanderlust Travels - Verify Your Email");

            String verifyUrl = baseUrl + "/verify?token=" + token;
            String htmlMsg = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 10px;\">"
                    + "<h2 style=\"color: #4f46e5; text-align: center;\">Welcome to Wanderlust Travels!</h2>"
                    + "<p>Thank you for registering. Please click the button below to verify your email address and activate your account:</p>"
                    + "<div style=\"text-align: center; margin: 30px 0;\">"
                    + "<a href=\"" + verifyUrl + "\" style=\"background-color: #4f46e5; color: white; padding: 12px 25px; text-decoration: none; border-radius: 25px; font-weight: bold;\">Verify Email</a>"
                    + "</div>"
                    + "<p>If the button doesn't work, you can copy and paste this link into your browser:</p>"
                    + "<p style=\"word-break: break-all; color: #6b7280; font-size: 14px;\">" + verifyUrl + "</p>"
                    + "<hr style=\"border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;\">"
                    + "<p style=\"color: #6b7280; font-size: 12px; text-align: center;\">&copy; 2026 Wanderlust Travels. All rights reserved.</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + to);
            e.printStackTrace();
        }
    }

    @Async
    public void sendPasswordResetEmail(String to, String token, String baseUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Wanderlust Travels - Reset Your Password");

            String resetUrl = baseUrl + "/reset-password?token=" + token;
            String htmlMsg = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 10px;\">"
                    + "<h2 style=\"color: #4f46e5; text-align: center;\">Reset Your Password</h2>"
                    + "<p>We received a request to reset your password. Click the button below to set a new password:</p>"
                    + "<div style=\"text-align: center; margin: 30px 0;\">"
                    + "<a href=\"" + resetUrl + "\" style=\"background-color: #4f46e5; color: white; padding: 12px 25px; text-decoration: none; border-radius: 25px; font-weight: bold;\">Reset Password</a>"
                    + "</div>"
                    + "<p>If you didn't request this, you can safely ignore this email. Your password will remain unchanged.</p>"
                    + "<p>This link will expire in 1 hour.</p>"
                    + "<hr style=\"border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;\">"
                    + "<p style=\"color: #6b7280; font-size: 12px; text-align: center;\">&copy; 2026 Wanderlust Travels. All rights reserved.</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send password reset email to " + to);
            e.printStackTrace();
        }
    }
}
