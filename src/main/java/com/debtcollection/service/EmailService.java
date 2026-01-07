package com.debtcollection.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class EmailService {

        private final JavaMailSender mailSender;

        public void sendOtp(String toEmail, String code) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper =
                        new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(toEmail);
                helper.setSubject("קוד אימות לכניסה למערכת");

                helper.setText(buildVerificationEmail(code), true);
                mailSender.send(message);

            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send verification email", e);
            }
        }
    private String buildVerificationEmail(String code) {
        return """
<!DOCTYPE html>
<html dir="rtl">
  <body style="
    margin: 0;
    padding: 40px 0;
  
    font-family: Arial;
    direction: rtl;
    text-align: center;
     
  ">

    <!-- Outer wrapper -->
    <div style="
      max-width: 600px;
      margin: 0 auto;
    
      
       background-color: var(--card-bg, #ffffff);
                      border-radius: 12px;
                      border: 1px solid var(--border-color, #e5e7eb);
                      padding: 32px;
                      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    ">

      <!-- Header -->
      <div style="
        padding: 28px;
        border-bottom: 1px solid #e5e7eb;
      ">
        <h1 style="
          margin: 0;
          color: #2563eb;
          font-size: 22px;
          font-weight: 700;
        ">
          🔐 קוד אימות
        </h1>
      </div>

      <!-- Content -->
      <div style="padding: 36px;">

        <p style="font-size: 16px; color: #334155; margin-top: 0;">
          שלום רב,
        </p>

        <p style="font-size: 16px; color: #334155;">
          קוד האימות שלך למערכת ניהול גביית תשלומים:
        </p>

        <!-- Code box -->
        <div style="
          background-color: #f9fafb;
          border: 1px solid #e5e7eb;
          border-radius: 14px;
          border-width: 1px;
          padding: 28px;
          margin: 32px 0;
        ">

          <p style="
            margin: 0;
            color: #6b7280;
            font-size: 14px;
          ">
            קוד האימות
          </p>

          <div style="
            font-size: 34px;
            font-weight: 700;
            color: #020817;
            letter-spacing: 8px;
            margin: 18px 0;
            font-family: monospace;
          ">
            %S
          </div>

          <p style="
            margin: 0;
            color: #f59e0b;
            font-size: 14px;
          ">
            ⏱️ תקף ל-10 דקות
          </p>
        </div>

        <!-- Tip / Warning -->
        <div style="
          background-color: #ffffff;
          border: 1px solid #e5e7eb;
          border-radius: 10px;
          padding: 14px 16px;
          margin: 24px 0;
          text-align: right;
          color: #374151;
          font-size: 14px;
        ">
          <strong>⚠️&nbsp;&nbsp;חשוב:</strong>
          אם לא ביקשת קוד זה, התעלם מהודעה.
        </div>

        <p style="
          color: #94a3b8;
          font-size: 13px;
          margin-top: 32px;
        ">
          מערכת ניהול גביית תשלומים © 2025
        </p>

      </div>
    </div>

  </body>
</html>
""".formatted(code);

    }
//<html dir='rtl'>
//                                                   <body style='font-family: Arial; direction: rtl;'>
//                                                     <div style='max-width: 600px; margin: auto; text-align: center;'>
//                                                       <h1 style='color: #0891b2;'>🔐 קוד אימות</h1>
//                                                     <div style='background: #f0f9ff; border: 2px dashed #06b6d4; border-radius: 12px; padding: 20px; margin: 20px 0;'>
//                                                        <p style='font-size: 14px; color: #64748b;'>קוד האימות שלך</p>
//                                                       <h2 style='font-size: 36px; color: #0891b2; letter-spacing: 10px; font-family: monospace;'>%S</h2>
//                                                       <p style='color: #f59e0b; font-size: 14px;'>⏱️ תקף ל-10 דקות</p>
//                                                    </div>
//                                                     <p style='color: #64748b;'>מערכת ניהול גביית תשלומים © 2025
//
//</p>
//                                                   </div>
//                                                   </body>
//                                                   </html>;

    }

