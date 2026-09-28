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
 * Configure spring.mail.* + app.mail.* in application.properties.
 * If sending fails the error is logged but swallowed — registration itself
 * must never fail just because the email couldn't go out.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${app.mail.from:Bazaari <no-reply@bazaari.com>}")
    private String fromAddress;

    @Value("${app.mail.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

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

            mailSender.send(message);
            log.info("Welcome email successfully sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error sending welcome email to {}: {}", user.getEmail(), e.getMessage(), e);
        }
    }

    private String safeName(User user) {
        return user.getUsername() != null ? user.getUsername() : "there";
    }

    private boolean isSeller(User user) {
        return user.getRole() != null && user.getRole().equalsIgnoreCase("SELLER");
    }

    /** Usernames/emails are user input, so escape before putting them in HTML. */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    private String buildPlainText(User user) {
        String next = isSeller(user)
                ? "Set up your storefront and list your first product."
                : "Start exploring handmade goods from independent sellers.";
        return "Hi " + safeName(user) + ",\n\n"
                + "Welcome to Bazaari! Your account has been created successfully.\n"
                + "Account type: " + user.getRole() + "\n"
                + "Email: " + user.getEmail() + "\n\n"
                + next + "\n" + baseUrl + "\n\n"
                + "— The Bazaari Team";
    }

    private String buildHtml(User user) {
        boolean seller = isSeller(user);

        String name = esc(safeName(user));
        String email = esc(user.getEmail());
        String role = seller ? "Seller" : "Customer";
        String roleBg = seller ? "#6E2C5C" : "#1F7A6C";

        String headline = seller ? "Your storefront starts here" : "Welcome to the bazaar";
        String intro = seller
                ? "Your seller account is ready. Set up your shop, list what you make, and ship straight to the people who want it."
                : "Your account is ready. Every listing on Bazaari ships directly from the person who makes or sources it.";
        String stepsTitle = seller ? "Getting your shop going" : "A few ways to begin";
        String step1 = seller ? "Add your first product with a photo, price and stock count."
                              : "Browse handloom textiles, studio ceramics and whole spices.";
        String step2 = seller ? "Keep an eye on stock levels from your seller dashboard."
                              : "Add favourites to your cart and pay with UPI, card or cash on delivery.";
        String step3 = seller ? "Watch new orders arrive and get them out the door."
                              : "Use a coupon at checkout if you have one.";
        String cta = seller ? "Open your dashboard" : "Start exploring";
        String preheader = seller ? "Your Bazaari seller account is ready."
                                  : "Your Bazaari account is ready. Come see what makers are selling.";

        String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <meta name="color-scheme" content="light">
          <title>Welcome to Bazaari</title>
          <link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,wght@0,500;0,600;1,500&family=Work+Sans:wght@400;500;600&display=swap" rel="stylesheet">
        </head>
        <body style="margin:0;padding:0;background-color:#F2F6F1;font-family:'Work Sans',-apple-system,BlinkMacSystemFont,'Segoe UI',Helvetica,Arial,sans-serif;color:#191330;">
          <div style="display:none;max-height:0;overflow:hidden;opacity:0;font-size:1px;line-height:1px;color:#F2F6F1;">{{preheader}}</div>

          <table width="100%" cellpadding="0" cellspacing="0" role="presentation" bgcolor="#F2F6F1" style="background-color:#F2F6F1;">
            <tr><td align="center" style="padding:32px 16px;">

              <table width="100%" cellpadding="0" cellspacing="0" role="presentation" style="max-width:600px;background-color:#ffffff;border-radius:14px;overflow:hidden;border:1px solid #dfe5de;">

                <!-- Colour strip (same four accents as the storefront) -->
                <tr><td>
                  <table width="100%" cellpadding="0" cellspacing="0" role="presentation"><tr>
                    <td width="25%" height="6" bgcolor="#F2A63C" style="font-size:0;line-height:0;">&nbsp;</td>
                    <td width="25%" height="6" bgcolor="#E85D4E" style="font-size:0;line-height:0;">&nbsp;</td>
                    <td width="25%" height="6" bgcolor="#1F7A6C" style="font-size:0;line-height:0;">&nbsp;</td>
                    <td width="25%" height="6" bgcolor="#6E2C5C" style="font-size:0;line-height:0;">&nbsp;</td>
                  </tr></table>
                </td></tr>

                <!-- Header -->
                <tr><td bgcolor="#191330" style="background-color:#191330;padding:40px 32px 36px;text-align:center;">
                  <div style="font-family:Fraunces,Georgia,'Times New Roman',serif;font-style:italic;font-weight:500;font-size:40px;letter-spacing:-0.02em;color:#F2F6F1;line-height:1;">Bazaar<span style="color:#F2A63C;">i</span></div>
                  <div style="margin-top:12px;font-size:14px;color:#c9c0dd;">Goods from makers, not warehouses.</div>
                </td></tr>

                <!-- Greeting -->
                <tr><td style="padding:40px 32px 8px;">
                  <h1 style="margin:0 0 6px;font-family:Fraunces,Georgia,'Times New Roman',serif;font-weight:500;font-size:30px;line-height:1.15;color:#191330;">{{headline}},<br>{{name}}.</h1>
                  <p style="margin:16px 0 0;font-size:16px;line-height:1.65;color:#3a3255;">{{intro}}</p>
                </td></tr>

                <!-- Account card -->
                <tr><td style="padding:24px 32px 8px;">
                  <table width="100%" cellpadding="0" cellspacing="0" role="presentation" bgcolor="#F2F6F1" style="background-color:#F2F6F1;border-radius:10px;border-left:4px solid #F2A63C;">
                    <tr><td style="padding:20px 22px;">
                      <div style="font-family:Fraunces,Georgia,serif;font-weight:600;font-size:17px;color:#191330;margin-bottom:12px;">Your account</div>
                      <table width="100%" cellpadding="0" cellspacing="0" role="presentation">
                        <tr>
                          <td width="90" style="padding:6px 0;font-size:14px;color:#6b6386;">Username</td>
                          <td style="padding:6px 0;font-size:14px;font-weight:600;color:#191330;">{{name}}</td>
                        </tr>
                        <tr>
                          <td style="padding:6px 0;font-size:14px;color:#6b6386;">Email</td>
                          <td style="padding:6px 0;font-size:14px;font-weight:600;color:#191330;word-break:break-all;">{{email}}</td>
                        </tr>
                        <tr>
                          <td style="padding:6px 0;font-size:14px;color:#6b6386;">Account</td>
                          <td style="padding:6px 0;"><span style="display:inline-block;background-color:{{roleBg}};color:#ffffff;padding:4px 14px;border-radius:999px;font-size:12px;font-weight:600;">{{role}}</span></td>
                        </tr>
                      </table>
                    </td></tr>
                  </table>
                </td></tr>

                <!-- Category tiles (mirror the storefront hero collage) -->
                <tr><td style="padding:24px 32px 0;">
                  <table width="100%" cellpadding="0" cellspacing="0" role="presentation"><tr>
                    <td width="32%" bgcolor="#E58A4A" style="background-color:#E58A4A;background-image:linear-gradient(150deg,#F2A63C,#E85D4E);border-radius:10px;padding:26px 10px;text-align:center;font-family:Fraunces,Georgia,serif;font-size:15px;font-weight:500;color:#ffffff;">Handloom<br>textiles</td>
                    <td width="2%">&nbsp;</td>
                    <td width="32%" bgcolor="#1F7A6C" style="background-color:#1F7A6C;background-image:linear-gradient(150deg,#1F7A6C,#19A88F);border-radius:10px;padding:26px 10px;text-align:center;font-family:Fraunces,Georgia,serif;font-size:15px;font-weight:500;color:#ffffff;">Studio<br>ceramics</td>
                    <td width="2%">&nbsp;</td>
                    <td width="32%" bgcolor="#6E2C5C" style="background-color:#6E2C5C;background-image:linear-gradient(150deg,#6E2C5C,#9B4B84);border-radius:10px;padding:26px 10px;text-align:center;font-family:Fraunces,Georgia,serif;font-size:15px;font-weight:500;color:#ffffff;">Whole<br>spices</td>
                  </tr></table>
                </td></tr>

                <!-- Steps -->
                <tr><td style="padding:32px 32px 0;">
                  <div style="font-family:Fraunces,Georgia,serif;font-weight:600;font-size:19px;color:#191330;margin-bottom:8px;">{{stepsTitle}}</div>
                  <table width="100%" cellpadding="0" cellspacing="0" role="presentation">
                    <tr>
                      <td width="20" valign="top" style="padding:12px 0 0;"><div style="width:10px;height:10px;border-radius:50%;background-color:#F2A63C;"></div></td>
                      <td style="padding:6px 0;font-size:15px;line-height:1.55;color:#3a3255;border-bottom:1px solid #eef1ed;">{{step1}}</td>
                    </tr>
                    <tr>
                      <td width="20" valign="top" style="padding:12px 0 0;"><div style="width:10px;height:10px;border-radius:50%;background-color:#1F7A6C;"></div></td>
                      <td style="padding:6px 0;font-size:15px;line-height:1.55;color:#3a3255;border-bottom:1px solid #eef1ed;">{{step2}}</td>
                    </tr>
                    <tr>
                      <td width="20" valign="top" style="padding:12px 0 0;"><div style="width:10px;height:10px;border-radius:50%;background-color:#6E2C5C;"></div></td>
                      <td style="padding:6px 0;font-size:15px;line-height:1.55;color:#3a3255;">{{step3}}</td>
                    </tr>
                  </table>
                </td></tr>

                <!-- CTA -->
                <tr><td align="center" style="padding:36px 32px 44px;">
                  <a href="{{baseUrl}}" style="display:inline-block;background-color:#F2A63C;color:#191330;text-decoration:none;font-weight:600;font-size:16px;padding:15px 38px;border-radius:999px;">{{cta}}</a>
                </td></tr>

                <!-- Footer -->
                <tr><td bgcolor="#191330" style="background-color:#191330;padding:28px 32px;text-align:center;">
                  <div style="font-family:Fraunces,Georgia,serif;font-style:italic;font-size:20px;color:#F2F6F1;">Bazaar<span style="color:#F2A63C;">i</span></div>
                  <p style="margin:12px 0 0;font-size:13px;line-height:1.6;color:#c9c0dd;">If you didn't create this account, you can safely ignore this email.</p>
                  <p style="margin:8px 0 0;font-size:12px;color:#8f86a8;">&copy; 2026 Bazaari Marketplace. All rights reserved.</p>
                </td></tr>

              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """;

        return html
                .replace("{{preheader}}", esc(preheader))
                .replace("{{headline}}", esc(headline))
                .replace("{{intro}}", esc(intro))
                .replace("{{stepsTitle}}", esc(stepsTitle))
                .replace("{{step1}}", esc(step1))
                .replace("{{step2}}", esc(step2))
                .replace("{{step3}}", esc(step3))
                .replace("{{cta}}", esc(cta))
                .replace("{{roleBg}}", roleBg)
                .replace("{{role}}", role)
                .replace("{{baseUrl}}", esc(baseUrl))
                .replace("{{name}}", name)
                .replace("{{email}}", email);
    }
}