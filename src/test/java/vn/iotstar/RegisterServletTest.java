package vn.iotstar;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.iotstar.controller.RegisterServlet;
import vn.iotstar.entity.User;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServletTest {

    private RegisterServlet servlet;

    @BeforeEach
    public void setup() {
        servlet = new RegisterServlet();
        vn.iotstar.dao.UserDao userDao = new vn.iotstar.dao.impl.UserDaoImpl();
        User extra = userDao.findByUsername("test_user_reg");
        if (extra != null) {
            userDao.delete(extra.getId());
        }
    }

    @Test
    public void testDoGetStartsEmptyAndForwardsToRegisterJsp() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        String[] redirectedUrl = new String[1];
        String[] forwardedUrl = new String[1];

        HttpServletRequest req = createMockRequest("GET", "/register", sessionAttrs, requestAttrs, new HashMap<>(), redirectedUrl, forwardedUrl);
        HttpServletResponse resp = createMockResponse(redirectedUrl);

        servlet.service(req, resp);

        assertEquals("/register.jsp", forwardedUrl[0], "GET /register must forward to /register.jsp");
        assertNull(redirectedUrl[0], "GET /register should not redirect if not logged in");

        // Verify that credentials are NOT automatically populated into request
        assertNull(requestAttrs.get("defaultUsername"), "defaultUsername must NOT be populated");
        assertNull(requestAttrs.get("defaultPassword"), "defaultPassword must NOT be populated");
        assertNull(requestAttrs.get("username"), "username must start empty");
        assertNull(requestAttrs.get("password"), "password must start empty");
    }

    @Test
    public void testDoGetWhenAlreadyLoggedInRedirectsToHome() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        User loggedInUser = new User();
        loggedInUser.setUsername("existinguser");
        sessionAttrs.put("user", loggedInUser);

        Map<String, Object> requestAttrs = new HashMap<>();
        String[] redirectedUrl = new String[1];
        String[] forwardedUrl = new String[1];

        HttpServletRequest req = createMockRequest("GET", "/register", sessionAttrs, requestAttrs, new HashMap<>(), redirectedUrl, forwardedUrl);
        HttpServletResponse resp = createMockResponse(redirectedUrl);

        servlet.service(req, resp);

        assertNotNull(redirectedUrl[0]);
        assertTrue(redirectedUrl[0].endsWith("/"), "Logged-in user must be redirected to /");
        assertNull(forwardedUrl[0]);
    }

    @Test
    public void testDoPostEmptyUsernameFailsValidationAndNeverSetsPassword() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", "");
        params.put("email", "test@example.com");
        params.put("password", "Secret123");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockRequest("POST", "/register", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/register.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("error"));
        assertTrue(requestAttrs.get("error").toString().contains("Tên đăng nhập không được để trống"));

        // Security check: Password must NEVER be preserved in request attribute
        assertNull(requestAttrs.get("password"), "Password attribute MUST be null on validation failure");
    }

    @Test
    public void testDoPostEmptyPasswordFailsValidationAndNeverSetsPassword() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", "myuser");
        params.put("email", "test@example.com");
        params.put("password", "");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockRequest("POST", "/register", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/register.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("error"));
        assertTrue(requestAttrs.get("error").toString().contains("Mật khẩu không được để trống"));

        // Username is preserved for convenience
        assertEquals("myuser", requestAttrs.get("username"));
        // Password MUST NOT be preserved
        assertNull(requestAttrs.get("password"), "Password attribute MUST be null");
    }

    @Test
    public void testDoPostEmptyEmailFailsValidationAndNeverSetsPassword() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", "myuser");
        params.put("email", "");
        params.put("password", "Pass12345");

        String[] forwardedUrl = new String[1];
        HttpServletRequest req = createMockRequest("POST", "/register", sessionAttrs, requestAttrs, params, new String[1], forwardedUrl);
        HttpServletResponse resp = createMockResponse(new String[1]);

        servlet.service(req, resp);

        assertEquals("/register.jsp", forwardedUrl[0]);
        assertNotNull(requestAttrs.get("error"));
        assertTrue(requestAttrs.get("error").toString().contains("Email không được để trống"));

        // Username is preserved for convenience
        assertEquals("myuser", requestAttrs.get("username"));
        // Password MUST NOT be preserved
        assertNull(requestAttrs.get("password"), "Password attribute MUST be null");
    }

    private HttpServletRequest createMockRequest(String method, String uri,
                                                 Map<String, Object> sessionAttrs,
                                                 Map<String, Object> requestAttrs,
                                                 Map<String, String> params,
                                                 String[] redirectedUrl,
                                                 String[] forwardedUrl) {
        HttpSession session = (HttpSession) Proxy.newProxyInstance(
                HttpSession.class.getClassLoader(),
                new Class<?>[]{HttpSession.class},
                (proxy, m, args) -> {
                    String name = m.getName();
                    if ("getAttribute".equals(name)) return sessionAttrs.get(args[0]);
                    if ("setAttribute".equals(name)) { sessionAttrs.put((String) args[0], args[1]); return null; }
                    if ("removeAttribute".equals(name)) { sessionAttrs.remove(args[0]); return null; }
                    if ("invalidate".equals(name)) { sessionAttrs.clear(); return null; }
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
                    if ("setAttribute".equals(name)) { requestAttrs.put((String) args[0], args[1]); return null; }
                    if ("getParameter".equals(name)) return params.get(args[0]);
                    if ("setCharacterEncoding".equals(name)) return null;
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
