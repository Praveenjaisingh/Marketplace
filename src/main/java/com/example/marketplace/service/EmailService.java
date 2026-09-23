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
            log.info("Preparing to send welcome email to: {}", user.getEmail());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Bazaari, " + safeName(user) + "!");
            helper.setText(buildPlainText(user), buildHtml(user));
            
            log.info("Executing mailSender.send() via Gmail SMTP...");
            mailSender.send(message);
            log.info("✅ Welcome email successfully sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("❌ CRITICAL ERROR sending welcome email to {}: {}", user.getEmail(), e.getMessage(), e);
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
        String name = safeName(user);
        String email = user.getEmail() != null ? user.getEmail() : "";
        String role = user.getRole() != null ? user.getRole().toUpperCase() : "CUSTOMER";
        
        String htmlTemplate = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Welcome to Bazaari</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f4f7f6; color: #333333;">
            <table width="100%" cellpadding="0" cellspacing="0" role="presentation" style="background-color: #f4f7f6; padding: 40px 20px;">
                <tr>
                    <td align="center">
                        <!-- Main Container -->
                        <table width="100%" cellpadding="0" cellspacing="0" role="presentation" style="max-width: 600px; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 24px rgba(0,0,0,0.05);">
                            
                            <!-- Header / Banner -->
                            <tr>
                                <td style="background-color: #0f172a; padding: 40px 30px; text-align: center;">
                                    <h1 style="color: #ffffff; margin: 0; font-size: 32px; font-weight: 800; letter-spacing: 2px;">BAZAARI</h1>
                                    <p style="color: #94a3b8; margin: 10px 0 0 0; font-size: 15px;">The Independent Marketplace</p>
                                </td>
                            </tr>
                            
                            <!-- Body Content -->
                            <tr>
                                <td style="padding: 40px 30px;">
                                    <h2 style="margin: 0 0 24px 0; font-size: 24px; color: #0f172a; font-weight: 700;">Welcome, {name}! 👋</h2>
                                    <p style="margin: 0 0 28px 0; font-size: 16px; line-height: 1.6; color: #475569;">
                                        Your account has been successfully created. We are thrilled to have you join our community of independent makers and conscious shoppers.
                                    </p>
                                    
                                    <!-- Account Details Card -->
                                    <table width="100%" cellpadding="0" cellspacing="0" role="presentation" style="background-color: #f8fafc; border-radius: 12px; border: 1px solid #e2e8f0; margin-bottom: 32px;">
                                        <tr>
                                            <td style="padding: 24px;">
                                                <h3 style="margin: 0 0 16px 0; font-size: 13px; text-transform: uppercase; letter-spacing: 1.2px; color: #64748b; font-weight: 700;">Your Account Details</h3>
                                                <table width="100%" cellpadding="0" cellspacing="0" role="presentation">
                                                    <tr>
                                                        <td style="padding: 8px 0; font-size: 15px; color: #64748b; width: 35%;"><strong>Username:</strong></td>
                                                        <td style="padding: 8px 0; font-size: 15px; color: #0f172a; font-weight: 600;">{name}</td>
                                                    </tr>
                                                    <tr>
                                                        <td style="padding: 8px 0; font-size: 15px; color: #64748b;"><strong>Email:</strong></td>
                                                        <td style="padding: 8px 0; font-size: 15px; color: #0f172a; font-weight: 600;">{email}</td>
                                                    </tr>
                                                    <tr>
                                                        <td style="padding: 8px 0; font-size: 15px; color: #64748b;"><strong>Role:</strong></td>
                                                        <td style="padding: 8px 0;">
                                                            <span style="background-color: #dbeafe; color: #1e40af; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">{role}</span>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    
                                    <p style="margin: 0 0 32px 0; font-size: 16px; line-height: 1.6; color: #475569;">
                                        Start exploring handloom textiles, studio ceramics, whole spices, and more — every listing ships directly from the creator.
                                    </p>
                                    
                                    <!-- Call to Action -->
                                    <table width="100%" cellpadding="0" cellspacing="0" role="presentation">
                                        <tr>
                                            <td align="center">
                                                <a href="#" style="display: inline-block; padding: 16px 36px; background-color: #0f172a; color: #ffffff; text-decoration: none; border-radius: 8px; font-size: 16px; font-weight: 600; text-align: center; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);">Start Exploring</a>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            
                            <!-- Footer -->
                            <tr>
                                <td style="background-color: #f1f5f9; padding: 32px 30px; text-align: center;">
                                    <p style="margin: 0 0 12px 0; font-size: 13px; color: #64748b; line-height: 1.5;">
                                        If you didn't create this account, you can safely ignore this email.
                                    </p>
                                    <p style="margin: 0; font-size: 13px; color: #94a3b8;">
                                        &copy; 2026 Bazaari Marketplace. All rights reserved.
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """;
        
        return htmlTemplate
                .replace("{name}", name)
                .replace("{email}", email)
                .replace("{role}", role);
    }
}
