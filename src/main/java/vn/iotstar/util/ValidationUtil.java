package vn.iotstar.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Lớp tiện ích kiểm tra tính hợp lệ của dữ liệu đầu vào.
 * Giữ gọn nhẹ, dễ dùng, không phụ thuộc vào framework bên ngoài.
 */
public final class ValidationUtil {

    private ValidationUtil() {
        // Private constructor để ngăn việc khởi tạo
    }

    // Email pattern thực tế: tiền tố hợp lệ @ tên miền có ít nhất 1 dấu chấm, TLD ít nhất 2 ký tự
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Username pattern: 3 đến 30 ký tự, chỉ gồm chữ cái, chữ số và gạch dưới, không khoảng trắng
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_]{3,30}$"
    );

    // OTP pattern: đúng 6 chữ số
    private static final Pattern OTP_PATTERN = Pattern.compile(
            "^[0-9]{6}$"
    );

    // Số điện thoại Việt Nam: 10 hoặc 11 chữ số, bắt đầu bằng số 0
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^0[0-9]{9,10}$"
    );

    // Các định dạng ảnh được phép tải lên
    public static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );

    // Dung lượng tối đa của ảnh (5 MB)
    public static final long MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;

    /**
     * Kiểm tra xem chuỗi có chứa ký tự điều khiển nguy hiểm (ASCII 0-31 ngoại trừ tab/newline nếu cần)
     */
    public static boolean hasControlCharacters(String s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // Ký tự điều khiển ASCII 0-31 (trừ \t, \n, \r) và ký tự DEL 127
            if (c < 32 && c != '\t' && c != '\n' && c != '\r') {
                return true;
            }
            if (c == 127) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra username hợp lệ:
     * - Không null/blank
     * - Độ dài từ 3 đến 30 ký tự
     * - Chỉ gồm ký tự chữ cái (a-z, A-Z), chữ số (0-9) và dấu gạch dưới (_)
     * - Không có khoảng trắng
     * - Không chứa ký tự điều khiển
     */
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        String trimmed = username.trim();
        if (trimmed.length() < 3 || trimmed.length() > 30) {
            return false;
        }
        if (hasControlCharacters(trimmed)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Kiểm tra password hợp lệ:
     * - Không null
     * - Độ dài từ 8 đến 100 ký tự
     * - Không được chỉ toàn ký tự trắng
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        if (password.trim().isEmpty()) {
            return false;
        }
        return password.length() >= 8 && password.length() <= 100;
    }

    /**
     * Kiểm tra email hợp lệ thực tế:
     * - Không null/blank
     * - Độ dài tối đa 100 ký tự
     * - Không chứa khoảng trắng hoặc ký tự điều khiển
     * - Đúng cấu trúc email thông dụng
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String trimmed = email.trim();
        if (trimmed.isEmpty() || trimmed.length() > 100) {
            return false;
        }
        if (hasControlCharacters(trimmed) || trimmed.contains(" ")) {
            return false;
        }
        return EMAIL_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Kiểm tra OTP hợp lệ:
     * - Không null
     * - Đúng 6 chữ số (0-9)
     */
    public static boolean isValidOtp(String otp) {
        if (otp == null) {
            return false;
        }
        String trimmed = otp.trim();
        return OTP_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Kiểm tra số điện thoại Việt Nam hợp lệ:
     * - Không null/blank
     * - Đúng định dạng 10 hoặc 11 chữ số, bắt đầu bằng 0
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        String trimmed = phone.trim();
        return PHONE_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Kiểm tra phần mở rộng file ảnh hợp lệ (.jpg, .jpeg, .png, .gif, .webp)
     */
    public static boolean isValidImageExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return false;
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0) {
            return false;
        }
        String ext = fileName.substring(dotIndex).toLowerCase();
        return ALLOWED_IMAGE_EXTENSIONS.contains(ext);
    }

    /**
     * Lấy phần mở rộng của file (dạng chữ thường, có kèm dấu chấm, ví dụ: .png)
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        return fileName.substring(dotIndex).toLowerCase();
    }

    /**
     * Kiểm tra MIME type của ảnh (bắt đầu bằng image/)
     */
    public static boolean isValidImageMime(String contentType) {
        if (contentType == null) {
            return false;
        }
        return contentType.trim().toLowerCase().startsWith("image/");
    }

    /**
     * Parse số thực không âm (BigDecimal >= 0).
     * Trả về null nếu không hợp lệ hoặc âm.
     */
    public static BigDecimal parseNonNegativeBigDecimal(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            BigDecimal val = new BigDecimal(s.trim());
            if (val.compareTo(BigDecimal.ZERO) < 0) {
                return null;
            }
            return val;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parse số nguyên không âm (Integer >= 0).
     * Trả về null nếu không hợp lệ hoặc âm hoặc có phần thập phân.
     */
    public static Integer parseNonNegativeInteger(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            String trimmed = s.trim();
            if (trimmed.contains(".") || trimmed.contains(",")) {
                return null;
            }
            int val = Integer.parseInt(trimmed);
            if (val < 0) {
                return null;
            }
            return val;
        } catch (Exception e) {
            return null;
        }
    }
}
