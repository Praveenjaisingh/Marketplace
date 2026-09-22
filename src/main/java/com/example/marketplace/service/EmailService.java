package com.example.marketplace.service;

import com.example.marketplace.entity.User;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Sends transactional emails (currently: the "welcome" email fired right
 * after a successful POST /api/users/create).
 *
 * Configure spring.mail.* + app.mail.* in application.properties with your
 * SMTP host / app password. If sending fails (e.g. credentials not filled
 * in yet, or app.mail.enabled=false) the error is logged but swallowed —
 * registration itself must never fail just because the email couldn't go out.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${app.mail.from:Bazaari <no-reply@bazaari.com>}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /** Fire-and-forget: runs on a background thread so registration never waits on SMTP. */
    public void sendWelcomeEmailAsync(User user) {
        Thread emailThread = new Thread(() -> sendWelcomeEmail(user), "welcome-email-" + user.getId());
        emailThread.setDaemon(true);
        emailThread.start();
    }

    void sendWelcomeEmail(User user) {
        if (!mailEnabled) {
            log.info("app.mail.enabled=false — skipping welcome email to {}", user.getEmail());
            return;
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Bazaari, " + safeName(user) + "!");
            helper.setText(buildPlainText(user), buildHtml(user));
            mailSender.send(message);
            log.info("Welcome email sent to {}", user.getEmail());
        } catch (Exception e) {
            // Never let a broken/unconfigured mail server fail user registration.
            log.warn("Could not send welcome email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private String safeName(User user) {
        return user.getUsername() != null ? user.getUsername() : "there";
    }

    private String buildPlainText(User user) {
        return "Hi " + safeName(user) + ",\n\n"
                + "Welcome to Bazaari! Your account has been created successfully.\n"
                + "Account type: " + user.getRole() + "\n"
                + "Email: " + user.getEmail() + "\n\n"
                + "Start exploring handmade goods from independent sellers.\n\n"
                + "— The Bazaari Team";
    }

    private String buildHtml(User user) {
        return "<div style=\"font-family:'Work Sans',Arial,sans-serif;max-width:480px;margin:0 auto;padding:32px 24px;color:#191330;\">"
                + "<h2 style=\"font-family:Georgia,serif;font-weight:600;margin-bottom:4px;\">Welcome to Bazaari, " + safeName(user) + " \uD83D\uDC4B</h2>"
                + "<p style=\"color:#3a3255;line-height:1.6;\">Your account has been created successfully. Here are your account details:</p>"
                + "<table style=\"width:100%;border-collapse:collapse;margin:16px 0;\">"
                + "<tr><td style=\"padding:8px 0;color:#3a3255;\">Username</td><td style=\"padding:8px 0;font-weight:600;\">" + safeName(user) + "</td></tr>"
                + "<tr><td style=\"padding:8px 0;color:#3a3255;\">Email</td><td style=\"padding:8px 0;font-weight:600;\">" + user.getEmail() + "</td></tr>"
                + "<tr><td style=\"padding:8px 0;color:#3a3255;\">Account type</td><td style=\"padding:8px 0;font-weight:600;\">" + user.getRole() + "</td></tr>"
                + "</table>"
                + "<p style=\"color:#3a3255;line-height:1.6;\">Browse handloom textiles, ceramics, spices and more — every listing ships directly from the maker.</p>"
                + "<p style=\"margin-top:24px;color:#9a93ab;font-size:12px;\">If you didn't create this account, you can safely ignore this email.</p>"
                + "</div>";
    }
}
