package vn.iotstar;

import org.junit.jupiter.api.Test;
import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.AuthService;
import vn.iotstar.util.EmailUtil;

import static org.junit.jupiter.api.Assertions.*;

public class EmailUtilTest {

    @Test
    public void testRegistrationSubjectCleanAndNoEmail() {
        String recipient = "user_secret_address@example.com";
        String subject = EmailUtil.getSubjectForPurpose("Kích hoạt tài khoản");

        assertEquals("Xác thực tài khoản MyShop – Mã OTP", subject);
        assertFalse(subject.contains(recipient));
        assertFalse(subject.contains("@"));
        assertFalse(subject.toLowerCase().contains("utegear"));
    }

    @Test
    public void testPasswordResetSubjectCleanAndNoEmail() {
        String recipient = "user_secret_address@example.com";
        String subject = EmailUtil.getSubjectForPurpose("Đặt lại mật khẩu");

        assertEquals("Đặt lại mật khẩu MyShop – Mã OTP", subject);
        assertFalse(subject.contains(recipient));
        assertFalse(subject.contains("@"));
        assertFalse(subject.toLowerCase().contains("utegear"));
    }

    @Test
    public void testRegistrationHtmlTemplateStructureAndPrivacy() {
        String recipient = "confidential_user@company.com";
        String otp = "654321";
        String actionTitle = "XÁC THỰC TÀI KHOẢN";
        String actionDesc = "Bạn vừa yêu cầu mã xác thực cho tài khoản tại MyShop.";

        String html = EmailUtil.buildOtpHtmlTemplate(actionTitle, actionDesc, otp, 5);

        // 1. Branding
        assertTrue(html.contains("MyShop"), "Must contain MyShop branding");
        assertTrue(html.contains("CỬA HÀNG MUA SẮM TRỰC TUYẾN"));
        assertFalse(html.toLowerCase().contains("utegear"), "Must NOT contain any UTEGear branding");

        // 2. Action title and description
        assertTrue(html.contains("XÁC THỰC TÀI KHOẢN"));
        assertTrue(html.contains(actionDesc));

        // 3. OTP visible & selectable
        assertTrue(html.contains("654321"), "Must contain OTP value");
        assertTrue(html.contains("user-select: all"), "OTP must be easily selectable");

        // 4. Expiration time
        assertTrue(html.contains("Mã OTP có hiệu lực trong 5 phút."));

        // 5. Security notice
        assertTrue(html.contains("Vui lòng không chia sẻ mã OTP này với bất kỳ ai, kể cả nhân viên MyShop."));
        assertTrue(html.contains("Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email."));

        // 6. Sign-off
        assertTrue(html.contains("MyShop Team"));

        // 7. Privacy: recipient email must NOT be present anywhere in HTML
        assertFalse(html.contains(recipient), "Recipient raw email must NOT appear in HTML");

        // 8. Proper HTML structure
        assertTrue(html.startsWith("<!DOCTYPE html>"));
        assertTrue(html.contains("<html>") || html.contains("<html lang=\"vi\">"));
        assertTrue(html.contains("</html>"));
    }

    @Test
    public void testPasswordResetHtmlTemplateStructureAndPrivacy() {
        String recipient = "another_confidential@domain.org";
        String otp = "987123";
        String actionTitle = "ĐẶT LẠI MẬT KHẨU";
        String actionDesc = "Bạn vừa yêu cầu mã xác thực để đặt lại mật khẩu cho tài khoản tại MyShop.";

        String html = EmailUtil.buildOtpHtmlTemplate(actionTitle, actionDesc, otp, 5);

        assertTrue(html.contains("MyShop"));
        assertTrue(html.contains("ĐẶT LẠI MẬT KHẨU"));
        assertTrue(html.contains(actionDesc));
        assertTrue(html.contains("987123"));
        assertTrue(html.contains("Mã OTP có hiệu lực trong 5 phút."));
        assertFalse(html.contains(recipient));
        assertFalse(html.toLowerCase().contains("utegear"));
    }

    @Test
    public void testSendRealOtpEmailIfConfigured() {
        if (EmailUtil.isMailConfigured()) {
            String testRecipient = EmailUtil.getProperty("MAIL_USERNAME", null);
            assertNotNull(testRecipient);
            assertDoesNotThrow(() -> {
                EmailUtil.sendOtp(testRecipient, "889900", "Kích hoạt tài khoản");
            }, "Sending real registration OTP email should succeed without exception");

            assertDoesNotThrow(() -> {
                EmailUtil.sendOtp(testRecipient, "112233", "Đặt lại mật khẩu");
            }, "Sending real password reset OTP email should succeed without exception");
        }
    }

    @Test
    public void testOtpVerificationAndResetPasswordFlow() {
        AuthService authService = new AuthService();
        UserDao userDao = new UserDaoImpl();

        String testEmail = "test_otp_flow_" + System.currentTimeMillis() + "@example.com";
        String testUser = "flowuser_" + System.currentTimeMillis();

        // 1. Register user
        String error = authService.register(testUser, testEmail, "InitialPass123");
        // May succeed or fail on real email dispatch if domain is mock, but let's test with valid email or mock user
        User created = userDao.findByEmail(testEmail);
        if (created != null) {
            try {
                // Check OTP generated
                assertNotNull(created.getOtp(), "Registration OTP must be generated");
                assertNotNull(created.getOtpExpiry(), "Registration OTP expiry must be set");
                assertFalse(created.isActive(), "User must be inactive prior to OTP verification");

                // 2. Verify OTP
                boolean verified = authService.verifyOtp(testEmail, created.getOtp());
                assertTrue(verified, "verifyOtp must succeed with correct OTP");

                User activeUser = userDao.findByEmail(testEmail);
                assertTrue(activeUser.isActive(), "User must be active after OTP verification");
                assertNull(activeUser.getOtp(), "OTP must be cleared after verification");

                // 3. Send reset OTP
                boolean resetSent = authService.sendResetOtp(testEmail);
                assertTrue(resetSent, "sendResetOtp must succeed");

                User resetUser = userDao.findByEmail(testEmail);
                assertNotNull(resetUser.getResetOtp(), "Reset OTP must be set");

                // 4. Reset password
                boolean passReset = authService.resetPassword(testEmail, resetUser.getResetOtp(), "NewPass456");
                assertTrue(passReset, "resetPassword must succeed with correct reset OTP");

                User finalUser = userDao.findByEmail(testEmail);
                assertNull(finalUser.getResetOtp(), "Reset OTP must be cleared after password reset");

                // 5. Test login with new password
                User loggedIn = authService.login(testUser, "NewPass456");
                assertNotNull(loggedIn, "User must be able to log in with new password");
            } finally {
                userDao.delete(created.getId());
            }
        }
    }
}
