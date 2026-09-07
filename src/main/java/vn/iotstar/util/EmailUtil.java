package vn.iotstar.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {

    private static final String DEFAULT_HOST = "smtp.gmail.com";
    private static final String DEFAULT_PORT = "587";
    private static final String CONFIG_FILE = "mail.properties";

    /**
     * Resolves a configuration key in priority order:
     * 1. System property (-Dkey=value)
     * 2. OS Environment variable
     * 3. Classpath mail.properties file
     * 4. Fallback default
     */
    public static String getProperty(String key, String fallback) {
        // 1. System property (try key as-is, uppercase_underscore, and lowercase.dot)
        String val = System.getProperty(key);
        if (val == null || val.isBlank()) {
            val = System.getProperty(key.toUpperCase().replace('.', '_'));
        }
        if (val == null || val.isBlank()) {
            val = System.getProperty(key.toLowerCase().replace('_', '.'));
        }
        if (val != null && !val.isBlank()) {
            return val.trim();
        }

        // 2. OS Environment variable
        String envKey = key.toUpperCase().replace('.', '_');
        val = System.getenv(envKey);
        if (val != null && !val.isBlank()) {
            return val.trim();
        }

        // 3. Classpath mail.properties
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                val = props.getProperty(key);
                if (val == null || val.isBlank()) {
                    val = props.getProperty(envKey);
                }
                if (val == null || val.isBlank()) {
                    val = props.getProperty(key.toLowerCase().replace('_', '.'));
                }
                if (val != null && !val.isBlank()) {
                    return val.trim();
                }
            }
        } catch (Exception ignored) {
        }

        // Try also via class loader of EmailUtil
        try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                val = props.getProperty(key);
                if (val == null || val.isBlank()) {
                    val = props.getProperty(envKey);
                }
                if (val == null || val.isBlank()) {
                    val = props.getProperty(key.toLowerCase().replace('_', '.'));
                }
                if (val != null && !val.isBlank()) {
                    return val.trim();
                }
            }
        } catch (Exception ignored) {
        }

        return fallback;
    }

    public static boolean isMailConfigured() {
        String username = getProperty("MAIL_USERNAME", null);
        String password = getProperty("MAIL_PASSWORD", null);
        return username != null && !username.isBlank() && password != null && !password.isBlank();
    }

    public static void sendOtp(
            String recipient,
            String otp,
            String purpose) {

        String host = getProperty("MAIL_HOST", DEFAULT_HOST);
        String port = getProperty("MAIL_PORT", DEFAULT_PORT);
        String username = getProperty("MAIL_USERNAME", null);
        String password = getProperty("MAIL_PASSWORD", null);
        String from = getProperty("MAIL_FROM", username);

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "MAIL_USERNAME / MAIL_PASSWORD chưa được cấu hình. Vui lòng cấu hình tài khoản gửi thư trong mail.properties hoặc biến môi trường.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.connectiontimeout", "7000");
        props.put("mail.smtp.timeout", "7000");
        props.put("mail.smtp.writetimeout", "7000");

        final String authUser = username;
        final String authPass = password;

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authUser, authPass);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            String sender = (from != null && !from.isBlank()) ? from : username;
            try {
                message.setFrom(new InternetAddress(sender, "MyShop", "UTF-8"));
            } catch (Exception e) {
                message.setFrom(new InternetAddress(sender));
            }

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            String subject = getSubjectForPurpose(purpose);
            message.setSubject(subject, "UTF-8");

            String actionTitle;
            String actionDescription;
            String normalized = (purpose != null) ? purpose.toLowerCase().trim() : "";
            if (normalized.contains("đặt lại mật khẩu") || normalized.contains("reset")) {
                actionTitle = "ĐẶT LẠI MẬT KHẨU";
                actionDescription = "Bạn vừa yêu cầu mã xác thực để đặt lại mật khẩu cho tài khoản tại MyShop.";
            } else {
                actionTitle = "XÁC THỰC TÀI KHOẢN";
                actionDescription = "Bạn vừa yêu cầu mã xác thực cho tài khoản tại MyShop.";
            }

            String htmlContent = buildOtpHtmlTemplate(actionTitle, actionDescription, otp, 5);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email OTP đến " + recipient + ": " + e.getMessage(), e);
        }
    }

    public static String getSubjectForPurpose(String purpose) {
        String normalized = (purpose != null) ? purpose.toLowerCase().trim() : "";
        if (normalized.contains("đặt lại mật khẩu") || normalized.contains("reset")) {
            return "Đặt lại mật khẩu MyShop – Mã OTP";
        }
        return "Xác thực tài khoản MyShop – Mã OTP";
    }

    public static String buildOtpHtmlTemplate(String actionTitle, String actionDescription, String otp, int expiryMinutes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"vi\">\n");
        sb.append("<head>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("<title>MyShop</title>\n");
        sb.append("</head>\n");
        sb.append("<body style=\"margin: 0; padding: 0; background-color: #f4f6f9; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #2d3748; -webkit-font-smoothing: antialiased;\">\n");
        sb.append("<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"background-color: #f4f6f9; padding: 30px 15px;\">\n");
        sb.append("  <tr>\n");
        sb.append("    <td align=\"center\">\n");
        sb.append("      <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 560px; background-color: #ffffff; border-radius: 8px; border: 1px solid #e2e8f0; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); overflow: hidden;\">\n");
        sb.append("        <tr>\n");
        sb.append("          <td style=\"background-color: #1a202c; padding: 24px 30px; text-align: center;\">\n");
        sb.append("            <div style=\"color: #ffffff; font-size: 24px; font-weight: bold; letter-spacing: 1.5px;\">MyShop</div>\n");
        sb.append("            <div style=\"color: #cbd5e0; font-size: 13px; margin-top: 4px; letter-spacing: 0.5px;\">CỬA HÀNG MUA SẮM TRỰC TUYẾN</div>\n");
        sb.append("          </td>\n");
        sb.append("        </tr>\n");
        sb.append("        <tr>\n");
        sb.append("          <td style=\"padding: 35px 35px 25px 35px;\">\n");
        sb.append("            <div style=\"display: inline-block; background-color: #edf2f7; color: #2b6cb0; font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 1px; padding: 5px 12px; border-radius: 4px; margin-bottom: 20px;\">\n");
        sb.append("              ").append(actionTitle).append("\n");
        sb.append("            </div>\n");
        sb.append("            <h2 style=\"margin: 0 0 16px 0; font-size: 18px; color: #1a202c; font-weight: 600;\">Xin chào,</h2>\n");
        sb.append("            <p style=\"margin: 0 0 20px 0; font-size: 15px; line-height: 1.6; color: #4a5568;\">\n");
        sb.append("              ").append(actionDescription).append("\n");
        sb.append("            </p>\n");
        sb.append("            <p style=\"margin: 0 0 10px 0; font-size: 14px; font-weight: 600; color: #4a5568;\">\n");
        sb.append("              Mã OTP của bạn:\n");
        sb.append("            </p>\n");
        sb.append("            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin: 15px 0 22px 0;\">\n");
        sb.append("              <tr>\n");
        sb.append("                <td align=\"center\" style=\"background-color: #f7fafc; border: 2px dashed #cbd5e0; border-radius: 8px; padding: 18px 24px;\">\n");
        sb.append("                  <span style=\"font-family: 'Courier New', Courier, monospace, sans-serif; font-size: 34px; font-weight: 700; color: #2b6cb0; letter-spacing: 10px; display: inline-block; user-select: all; -webkit-user-select: all;\">").append(otp).append("</span>\n");
        sb.append("                </td>\n");
        sb.append("              </tr>\n");
        sb.append("            </table>\n");
        sb.append("            <p style=\"margin: 0 0 20px 0; font-size: 14px; color: #e53e3e; font-weight: 600;\">\n");
        sb.append("              Mã OTP có hiệu lực trong ").append(expiryMinutes).append(" phút.\n");
        sb.append("            </p>\n");
        sb.append("            <div style=\"background-color: #fffaf0; border-left: 4px solid #dd6b20; padding: 12px 16px; border-radius: 0 4px 4px 0; margin-bottom: 25px;\">\n");
        sb.append("              <p style=\"margin: 0 0 4px 0; font-size: 13px; color: #9c4221; font-weight: 600;\">\n");
        sb.append("                Lưu ý bảo mật:\n");
        sb.append("              </p>\n");
        sb.append("              <p style=\"margin: 0; font-size: 13px; line-height: 1.5; color: #7b341e;\">\n");
        sb.append("                Vui lòng không chia sẻ mã OTP này với bất kỳ ai, kể cả nhân viên MyShop. Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.\n");
        sb.append("              </p>\n");
        sb.append("            </div>\n");
        sb.append("            <p style=\"margin: 0; font-size: 14px; line-height: 1.6; color: #4a5568;\">\n");
        sb.append("              Trân trọng,<br>\n");
        sb.append("              <strong style=\"color: #1a202c;\">MyShop Team</strong>\n");
        sb.append("            </p>\n");
        sb.append("          </td>\n");
        sb.append("        </tr>\n");
        sb.append("        <tr>\n");
        sb.append("          <td style=\"background-color: #f7fafc; border-top: 1px solid #edf2f7; padding: 20px 30px; text-align: center;\">\n");
        sb.append("            <p style=\"margin: 0 0 6px 0; font-size: 12px; color: #a0aec0; line-height: 1.4;\">\n");
        sb.append("              Đây là email tự động từ hệ thống MyShop. Vui lòng không trả lời trực tiếp email này.\n");
        sb.append("            </p>\n");
        sb.append("            <p style=\"margin: 0; font-size: 12px; color: #a0aec0;\">\n");
        sb.append("              &copy; 2026 MyShop. All rights reserved.\n");
        sb.append("            </p>\n");
        sb.append("          </td>\n");
        sb.append("        </tr>\n");
        sb.append("      </table>\n");
        sb.append("    </td>\n");
        sb.append("  </tr>\n");
        sb.append("</table>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }
}