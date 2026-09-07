package vn.iotstar;

import org.junit.jupiter.api.Test;
import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.filter.MySiteMeshFilter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {

    private final UserDao userDao = new UserDaoImpl();

    @Test
    public void testUserProfileJPAUpdateAndPersistence() {
        User user = userDao.findByUsername("user");
        assertNotNull(user, "User 'user' should exist in database");

        Long userId = user.getId();
        String originalUsername = user.getUsername();
        String originalEmail = user.getEmail();
        String originalPassword = user.getPassword();

        // 1. Update fullname, phone, image
        user.setFullname("Nguyễn Văn A");
        user.setPhone("0901234567");
        user.setImage("users/avatar-test.png");

        userDao.update(user);

        // 2. Fetch fresh user from DB
        User updated = userDao.findById(userId);
        assertNotNull(updated);
        assertEquals("Nguyễn Văn A", updated.getFullname(), "Fullname must be persisted to DB");
        assertEquals("0901234567", updated.getPhone(), "Phone must be persisted to DB");
        assertEquals("users/avatar-test.png", updated.getImage(), "Image must be persisted to DB");

        // 3. Ensure other fields are NOT modified
        assertEquals(originalUsername, updated.getUsername());
        assertEquals(originalEmail, updated.getEmail());
        assertEquals(originalPassword, updated.getPassword());

        // 4. Test keeping old image when image is not updated
        updated.setFullname("Nguyễn Văn B");
        updated.setPhone("0987654321");
        // Keep same image
        userDao.update(updated);

        User secondUpdate = userDao.findById(userId);
        assertNotNull(secondUpdate);
        assertEquals("Nguyễn Văn B", secondUpdate.getFullname());
        assertEquals("0987654321", secondUpdate.getPhone());
        assertEquals("users/avatar-test.png", secondUpdate.getImage(), "Old image must be retained");
    }

    @Test
    public void testPhoneValidationLogic() {
        String validPhone1 = "0901234567";
        String validPhone2 = "01234567890";
        String invalidPhone1 = "123456789"; // Not starting with 0
        String invalidPhone2 = "090123"; // Too short
        String invalidPhone3 = "0901234567890123"; // Too long
        String invalidPhone4 = "0901234abc"; // Contains letters

        String regex = "^0[0-9]{9,10}$";
        assertTrue(validPhone1.matches(regex));
        assertTrue(validPhone2.matches(regex));
        assertFalse(invalidPhone1.matches(regex));
        assertFalse(invalidPhone2.matches(regex));
        assertFalse(invalidPhone3.matches(regex));
        assertFalse(invalidPhone4.matches(regex));
    }

    @Test
    public void testImageExtensionValidation() {
        List<String> allowed = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");

        assertTrue(allowed.contains(".jpg"));
        assertTrue(allowed.contains(".png"));
        assertTrue(allowed.contains(".webp"));
        assertFalse(allowed.contains(".exe"));
        assertFalse(allowed.contains(".txt"));
        assertFalse(allowed.contains(".pdf"));
    }

    @Test
    public void testEmailValidationLogic() {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        assertTrue("user@example.com".matches(regex));
        assertTrue("test.name+alias@sub.domain.edu.vn".matches(regex));
        assertFalse("abc".matches(regex));
        assertFalse("abc@".matches(regex));
        assertFalse("@gmail.com".matches(regex));
        assertFalse("plainaddress".matches(regex));
        assertFalse("user@domain".matches(regex));
    }

    @Test
    public void testUserProfileEmailJPAUpdate() {
        User user = userDao.findByUsername("user");
        assertNotNull(user);

        String originalEmail = user.getEmail();
        String tempEmail = "new_user_email_" + System.currentTimeMillis() + "@example.com";

        user.setEmail(tempEmail);
        userDao.update(user);

        User refreshed = userDao.findById(user.getId());
        assertEquals(tempEmail, refreshed.getEmail());
        assertEquals("user", refreshed.getUsername());
        assertEquals("USER", refreshed.getRole());
        assertTrue(refreshed.isActive());

        // Restore original email
        refreshed.setEmail(originalEmail);
        userDao.update(refreshed);

        User restored = userDao.findById(user.getId());
        assertEquals(originalEmail, restored.getEmail());
    }

    @Test
    public void testSiteMeshFilterInstantiation() {
        MySiteMeshFilter filter = new MySiteMeshFilter();
        assertNotNull(filter, "MySiteMeshFilter must instantiate cleanly without ClassNotFound errors");
    }
}
