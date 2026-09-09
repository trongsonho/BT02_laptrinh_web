package vn.iotstar;

import org.junit.jupiter.api.Test;
import vn.iotstar.util.ValidationUtil;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    public void testIsValidUsername() {
        assertFalse(ValidationUtil.isValidUsername(null));
        assertFalse(ValidationUtil.isValidUsername(""));
        assertFalse(ValidationUtil.isValidUsername("   "));
        assertFalse(ValidationUtil.isValidUsername("ab")); // < 3
        assertFalse(ValidationUtil.isValidUsername("a".repeat(31))); // > 30
        assertFalse(ValidationUtil.isValidUsername("user name")); // contains space
        assertFalse(ValidationUtil.isValidUsername("user@123")); // contains special char
        assertFalse(ValidationUtil.isValidUsername("user#name"));
        assertFalse(ValidationUtil.isValidUsername("user\u0000name")); // control char

        assertTrue(ValidationUtil.isValidUsername("abc"));
        assertTrue(ValidationUtil.isValidUsername("user_123"));
        assertTrue(ValidationUtil.isValidUsername("admin"));
        assertTrue(ValidationUtil.isValidUsername("Admin_99"));
    }

    @Test
    public void testIsValidPassword() {
        assertFalse(ValidationUtil.isValidPassword(null));
        assertFalse(ValidationUtil.isValidPassword(""));
        assertFalse(ValidationUtil.isValidPassword("       "));
        assertFalse(ValidationUtil.isValidPassword("1234567")); // < 8
        assertFalse(ValidationUtil.isValidPassword("a".repeat(101))); // > 100

        assertTrue(ValidationUtil.isValidPassword("12345678"));
        assertTrue(ValidationUtil.isValidPassword("admin@123456"));
        assertTrue(ValidationUtil.isValidPassword("ValidPass123!"));
    }

    @Test
    public void testIsValidEmail() {
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("   "));
        assertFalse(ValidationUtil.isValidEmail("abc"));
        assertFalse(ValidationUtil.isValidEmail("abc@"));
        assertFalse(ValidationUtil.isValidEmail("@domain.com"));
        assertFalse(ValidationUtil.isValidEmail("abc@domain"));
        assertFalse(ValidationUtil.isValidEmail("abc @domain.com"));
        assertFalse(ValidationUtil.isValidEmail("a".repeat(95) + "@domain.com")); // > 100

        assertTrue(ValidationUtil.isValidEmail("user@example.com"));
        assertTrue(ValidationUtil.isValidEmail("admin_123@domain.org"));
        assertTrue(ValidationUtil.isValidEmail("first.last+tag@company.co.uk"));
    }

    @Test
    public void testIsValidOtp() {
        assertFalse(ValidationUtil.isValidOtp(null));
        assertFalse(ValidationUtil.isValidOtp(""));
        assertFalse(ValidationUtil.isValidOtp("12345")); // 5 digits
        assertFalse(ValidationUtil.isValidOtp("1234567")); // 7 digits
        assertFalse(ValidationUtil.isValidOtp("12AB56")); // letters
        assertFalse(ValidationUtil.isValidOtp("ABCDEF"));

        assertTrue(ValidationUtil.isValidOtp("000000"));
        assertTrue(ValidationUtil.isValidOtp("123456"));
        assertTrue(ValidationUtil.isValidOtp("999999"));
    }

    @Test
    public void testIsValidPhone() {
        assertFalse(ValidationUtil.isValidPhone(null));
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone("1234567890")); // doesn't start with 0
        assertFalse(ValidationUtil.isValidPhone("098765432")); // 9 digits
        assertFalse(ValidationUtil.isValidPhone("012345678901")); // 12 digits
        assertFalse(ValidationUtil.isValidPhone("0987abc321")); // letters

        assertTrue(ValidationUtil.isValidPhone("0987654321")); // 10 digits
        assertTrue(ValidationUtil.isValidPhone("01234567890")); // 11 digits
    }

    @Test
    public void testIsValidImageExtension() {
        assertFalse(ValidationUtil.isValidImageExtension(null));
        assertFalse(ValidationUtil.isValidImageExtension(""));
        assertFalse(ValidationUtil.isValidImageExtension("file.txt"));
        assertFalse(ValidationUtil.isValidImageExtension("script.exe"));
        assertFalse(ValidationUtil.isValidImageExtension("doc.pdf"));

        assertTrue(ValidationUtil.isValidImageExtension("avatar.jpg"));
        assertTrue(ValidationUtil.isValidImageExtension("PHOTO.JPEG"));
        assertTrue(ValidationUtil.isValidImageExtension("image.png"));
        assertTrue(ValidationUtil.isValidImageExtension("graphic.gif"));
        assertTrue(ValidationUtil.isValidImageExtension("modern.webp"));
    }

    @Test
    public void testIsValidImageMime() {
        assertFalse(ValidationUtil.isValidImageMime(null));
        assertFalse(ValidationUtil.isValidImageMime(""));
        assertFalse(ValidationUtil.isValidImageMime("text/plain"));
        assertFalse(ValidationUtil.isValidImageMime("application/json"));
        assertFalse(ValidationUtil.isValidImageMime("application/octet-stream"));

        assertTrue(ValidationUtil.isValidImageMime("image/jpeg"));
        assertTrue(ValidationUtil.isValidImageMime("image/png"));
        assertTrue(ValidationUtil.isValidImageMime("image/webp"));
    }

    @Test
    public void testParseNonNegativeBigDecimal() {
        assertNull(ValidationUtil.parseNonNegativeBigDecimal(null));
        assertNull(ValidationUtil.parseNonNegativeBigDecimal(""));
        assertNull(ValidationUtil.parseNonNegativeBigDecimal("abc"));
        assertNull(ValidationUtil.parseNonNegativeBigDecimal("-10.5"));

        assertEquals(BigDecimal.ZERO, ValidationUtil.parseNonNegativeBigDecimal("0"));
        assertEquals(new BigDecimal("150000"), ValidationUtil.parseNonNegativeBigDecimal("150000"));
        assertEquals(new BigDecimal("99.99"), ValidationUtil.parseNonNegativeBigDecimal(" 99.99 "));
    }

    @Test
    public void testParseNonNegativeInteger() {
        assertNull(ValidationUtil.parseNonNegativeInteger(null));
        assertNull(ValidationUtil.parseNonNegativeInteger(""));
        assertNull(ValidationUtil.parseNonNegativeInteger("xyz"));
        assertNull(ValidationUtil.parseNonNegativeInteger("-1"));
        assertNull(ValidationUtil.parseNonNegativeInteger("10.5"));

        assertEquals(Integer.valueOf(0), ValidationUtil.parseNonNegativeInteger("0"));
        assertEquals(Integer.valueOf(100), ValidationUtil.parseNonNegativeInteger(" 100 "));
    }
}
