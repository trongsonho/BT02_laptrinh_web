package vn.iotstar;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.iotstar.controller.ProfileServlet;
import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileServletTest {

    private ProfileServlet servlet;
    private UserDao userDao;
    private User testUser;

    @BeforeEach
    public void setup() {
        servlet = new ProfileServlet();
        userDao = new UserDaoImpl();
        testUser = userDao.findByUsername("user");
        assertNotNull(testUser);
    }

    @Test
    public void testDoGetUnauthenticatedRedirectsToLogin() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        String[] redirectedUrl = new String[1];

        HttpServletRequest req = createMockRequest("/profile", sessionAttrs, requestAttrs, new HashMap<>(), redirectedUrl);
        HttpServletResponse resp = createMockResponse(redirectedUrl);

        servlet.service(req, resp);

        assertNotNull(redirectedUrl[0]);
        assertTrue(redirectedUrl[0].endsWith("/login"), "Unauthenticated user must be redirected to /login");
    }

    @Test
    public void testDoGetAuthenticatedForwardsToProfileJsp() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("user", testUser);
        Map<String, Object> requestAttrs = new HashMap<>();
        String[] forwardedUrl = new String[1];

        HttpServletRequest req = createMockRequest("/profile", sessionAttrs, requestAttrs, new HashMap<>(), new String[1], forwardedUrl);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/profile.jsp", forwardedUrl[0], "Authenticated GET /profile must forward to /profile.jsp");
        assertNotNull(requestAttrs.get("user"));
        User viewUser = (User) requestAttrs.get("user");
        assertEquals(testUser.getId(), viewUser.getId());
    }

    @Test
    public void testDoPostValidationFailureDoesNotUpdateDB() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("user", testUser);
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("email", testUser.getEmail());
        params.put("fullname", "Nguyễn Văn Test");
        params.put("phone", "invalid-phone"); // invalid phone number

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockPostRequest("/profile", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl, null);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/profile.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("error"));
        assertTrue(requestAttrs.get("error").toString().contains("Số điện thoại không hợp lệ"));

        // Verify DB was NOT updated with the invalid phone
        User fresh = userDao.findById(testUser.getId());
        assertNotEquals("invalid-phone", fresh.getPhone());
    }

    @Test
    public void testDoPostEmptyEmailReturnsError() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("user", testUser);
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("email", "");
        params.put("fullname", "Test Name");
        params.put("phone", "0901234567");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockPostRequest("/profile", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl, null);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/profile.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("error"));
        assertEquals("Email không được để trống.", requestAttrs.get("error"));
    }

    @Test
    public void testDoPostInvalidEmailFormatReturnsError() throws Exception {
        String[] invalidEmails = {"abc", "abc@", "@gmail.com", "plainaddress"};

        for (String invalidEmail : invalidEmails) {
            Map<String, Object> sessionAttrs = new HashMap<>();
            sessionAttrs.put("user", testUser);
            Map<String, Object> requestAttrs = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            params.put("email", invalidEmail);
            params.put("fullname", "Test Name");
            params.put("phone", "0901234567");

            String[] forwardedUrl = new String[1];
            HttpServletRequest req = createMockPostRequest("/profile", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl, null);
            HttpServletResponse resp = createMockResponse(new String[1]);

            servlet.service(req, resp);

            assertEquals("/profile.jsp", forwardedUrl[0]);
            assertNotNull(requestAttrs.get("error"));
            assertEquals("Email không hợp lệ.", requestAttrs.get("error"), "Should reject invalid email: " + invalidEmail);
        }
    }

    @Test
    public void testDoPostDuplicateEmailBelongingToAnotherUserReturnsError() throws Exception {
        User admin = userDao.findByUsername("admin");
        assertNotNull(admin, "Admin user must exist");
        String adminEmail = admin.getEmail();
        assertNotNull(adminEmail);

        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("user", testUser);
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("email", adminEmail); // attempt to steal admin's email
        params.put("fullname", "Test Name");
        params.put("phone", "0901234567");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockPostRequest("/profile", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl, null);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/profile.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("error"));
        assertEquals("Email này đã được sử dụng bởi tài khoản khác.", requestAttrs.get("error"));

        // Verify DB unchanged for testUser
        User fresh = userDao.findById(testUser.getId());
        assertNotEquals(adminEmail, fresh.getEmail());
    }

    @Test
    public void testDoPostSameEmailSucceeds() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("user", testUser);
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("email", testUser.getEmail()); // keeping own current email
        params.put("fullname", "Trần Thị Lan");
        params.put("phone", "0912345678");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockPostRequest("/profile", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl, null);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/profile.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("message"));
        assertEquals("Cập nhật hồ sơ thành công!", requestAttrs.get("message"));
    }

    @Test
    public void testDoPostCaseVariationEmailSucceedsAndNormalized() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("user", testUser);
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        // Submit uppercase version of user's own email
        params.put("email", testUser.getEmail().toUpperCase());
        params.put("fullname", "Trần Thị Lan");
        params.put("phone", "0912345678");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockPostRequest("/profile", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl, null);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/profile.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("message"));
        assertEquals("Cập nhật hồ sơ thành công!", requestAttrs.get("message"));

        User sessionUser = (User) sessionAttrs.get("user");
        assertEquals(testUser.getEmail().toLowerCase(), sessionUser.getEmail());
    }

    @Test
    public void testDoPostSuccessfulUpdateUpdatesDBAndSession() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("user", testUser);
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        String newEmail = "updated_" + System.currentTimeMillis() + "@example.com";
        params.put("email", newEmail);
        params.put("fullname", "Trần Thị Lan");
        params.put("phone", "0912345678");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockPostRequest("/profile", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl, null);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/profile.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("message"));
        assertEquals("Cập nhật hồ sơ thành công!", requestAttrs.get("message"));

        // Verify Session was updated with new email
        User sessionUser = (User) sessionAttrs.get("user");
        assertEquals(newEmail, sessionUser.getEmail());
        assertEquals("Trần Thị Lan", sessionUser.getFullname());
        assertEquals("0912345678", sessionUser.getPhone());

        // Verify DB was updated with new email
        User fresh = userDao.findById(testUser.getId());
        assertEquals(newEmail, fresh.getEmail());
        assertEquals("Trần Thị Lan", fresh.getFullname());
        assertEquals("0912345678", fresh.getPhone());

        // Restore original email
        fresh.setEmail(testUser.getEmail());
        userDao.update(fresh);
    }

    @Test
    public void testAvatarIsolationUserUpdateDoesNotAffectAdmin() {
        User admin = userDao.findByUsername("admin");
        User user = userDao.findByUsername("user");
        assertNotNull(admin, "Admin must exist");
        assertNotNull(user, "User must exist");

        String adminOriginalImage = admin.getImage();
        String userOriginalImage = user.getImage();

        // Simulate User updating their avatar
        String newUserAvatar = "users/user_isolated_" + System.currentTimeMillis() + ".png";
        user.setImage(newUserAvatar);
        userDao.update(user);

        // Verify Admin avatar remains unchanged
        User freshAdmin = userDao.findById(admin.getId());
        assertEquals(adminOriginalImage, freshAdmin.getImage(), "Admin image must NOT change when User updates avatar");

        // Verify User avatar changed
        User freshUser = userDao.findById(user.getId());
        assertEquals(newUserAvatar, freshUser.getImage());

        // Restore user original image
        freshUser.setImage(userOriginalImage);
        userDao.update(freshUser);
    }

    @Test
    public void testAvatarIsolationAdminUpdateDoesNotAffectUser() {
        User admin = userDao.findByUsername("admin");
        User user = userDao.findByUsername("user");
        assertNotNull(admin, "Admin must exist");
        assertNotNull(user, "User must exist");

        String adminOriginalImage = admin.getImage();
        String userOriginalImage = user.getImage();

        // Simulate Admin updating their avatar
        String newAdminAvatar = "users/admin_isolated_" + System.currentTimeMillis() + ".png";
        admin.setImage(newAdminAvatar);
        userDao.update(admin);

        // Verify User avatar remains unchanged
        User freshUser = userDao.findById(user.getId());
        assertEquals(userOriginalImage, freshUser.getImage(), "User image must NOT change when Admin updates avatar");

        // Verify Admin avatar changed
        User freshAdmin = userDao.findById(admin.getId());
        assertEquals(newAdminAvatar, freshAdmin.getImage());

        // Restore admin original image
        freshAdmin.setImage(adminOriginalImage);
        userDao.update(freshAdmin);
    }

    private HttpServletRequest createMockRequest(String uri, Map<String, Object> sessionAttrs,
                                                Map<String, Object> requestAttrs,
                                                Map<String, String> params,
                                                String[] redirectedUrl) {
        return createMockRequest(uri, sessionAttrs, requestAttrs, params, redirectedUrl, new String[1]);
    }

    private HttpServletRequest createMockRequest(String uri, Map<String, Object> sessionAttrs,
                                                Map<String, Object> requestAttrs,
                                                Map<String, String> params,
                                                String[] redirectedUrl,
                                                String[] forwardedUrl) {
        return createMockPostRequest("GET", uri, sessionAttrs, requestAttrs, params, redirectedUrl, forwardedUrl, null);
    }

    private HttpServletRequest createMockPostRequest(String uri, Map<String, Object> sessionAttrs,
                                                    Map<String, Object> requestAttrs,
                                                    Map<String, String> params,
                                                    String[] redirectedUrl,
                                                    String[] forwardedUrl,
                                                    Part imagePart) {
        return createMockPostRequest("POST", uri, sessionAttrs, requestAttrs, params, redirectedUrl, forwardedUrl, imagePart);
    }

    private HttpServletRequest createMockPostRequest(String method, String uri, Map<String, Object> sessionAttrs,
                                                    Map<String, Object> requestAttrs,
                                                    Map<String, String> params,
                                                    String[] redirectedUrl,
                                                    String[] forwardedUrl,
                                                    Part imagePart) {
        HttpSession session = (HttpSession) Proxy.newProxyInstance(
                HttpSession.class.getClassLoader(),
                new Class<?>[]{HttpSession.class},
                (proxy, m, args) -> {
                    String name = m.getName();
                    if ("getAttribute".equals(name)) {
                        return sessionAttrs.get(args[0]);
                    } else if ("setAttribute".equals(name)) {
                        sessionAttrs.put((String) args[0], args[1]);
                        return null;
                    } else if ("removeAttribute".equals(name)) {
                        sessionAttrs.remove(args[0]);
                        return null;
                    } else if ("invalidate".equals(name)) {
                        sessionAttrs.clear();
                        return null;
                    }
                    return null;
                }
        );

        RequestDispatcher dispatcher = (RequestDispatcher) Proxy.newProxyInstance(
                RequestDispatcher.class.getClassLoader(),
                new Class<?>[]{RequestDispatcher.class},
                (proxy, m, args) -> {
                    if ("forward".equals(m.getName()) || "include".equals(m.getName())) {
                        return null;
                    }
                    return null;
                }
        );

        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class},
                (proxy, m, args) -> {
                    String name = m.getName();
                    if ("getMethod".equals(name)) return method;
                    if ("getContextPath".equals(name)) return "";
                    if ("getRequestURI".equals(name)) return uri;
                    if ("getSession".equals(name)) {
                        boolean create = args.length == 0 || (boolean) args[0];
                        return (create || !sessionAttrs.isEmpty()) ? session : null;
                    }
                    if ("getAttribute".equals(name)) return requestAttrs.get(args[0]);
                    if ("setAttribute".equals(name)) {
                        requestAttrs.put((String) args[0], args[1]);
                        return null;
                    }
                    if ("getParameter".equals(name)) return params.get(args[0]);
                    if ("setCharacterEncoding".equals(name)) return null;
                    if ("getPart".equals(name)) return imagePart;
                    if ("getRequestDispatcher".equals(name)) {
                        forwardedUrl[0] = (String) args[0];
                        return dispatcher;
                    }
                    return null;
                }
        );
    }

    private HttpServletResponse createMockResponse(String[] redirectedUrl) {
        return (HttpServletResponse) Proxy.newProxyInstance(
                HttpServletResponse.class.getClassLoader(),
                new Class<?>[]{HttpServletResponse.class},
                (proxy, m, args) -> {
                    if ("sendRedirect".equals(m.getName())) {
                        redirectedUrl[0] = (String) args[0];
                        return null;
                    }
                    return null;
                }
        );
    }
}
