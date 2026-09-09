package vn.iotstar;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.iotstar.controller.LoginServlet;
import vn.iotstar.entity.User;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LoginValidationTest {

    private LoginServlet servlet;

    @BeforeEach
    public void setup() {
        servlet = new LoginServlet();
    }

    private HttpServletRequest createMockRequest(String method, String uri,
                                                 Map<String, Object> sessionAttrs,
                                                 Map<String, Object> requestAttrs,
                                                 Map<String, String> params,
                                                 String[] redirectedUrl,
                                                 String[] includedUrl) {
        HttpSession session = (HttpSession) Proxy.newProxyInstance(
                HttpSession.class.getClassLoader(),
                new Class[]{HttpSession.class},
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
                    }
                    return null;
                }
        );

        RequestDispatcher dispatcher = (RequestDispatcher) Proxy.newProxyInstance(
                RequestDispatcher.class.getClassLoader(),
                new Class[]{RequestDispatcher.class},
                (proxy, m, args) -> {
                    String name = m.getName();
                    if ("include".equals(name) || "forward".equals(name)) {
                        return null;
                    }
                    return null;
                }
        );

        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class[]{HttpServletRequest.class},
                (proxy, m, args) -> {
                    String name = m.getName();
                    if ("getMethod".equals(name)) return method;
                    if ("getRequestURI".equals(name)) return uri;
                    if ("getContextPath".equals(name)) return "/bt02_laptrinh_web";
                    if ("getParameter".equals(name)) return params.get(args[0]);
                    if ("getAttribute".equals(name)) return requestAttrs.get(args[0]);
                    if ("setAttribute".equals(name)) {
                        requestAttrs.put((String) args[0], args[1]);
                        return null;
                    }
                    if ("getSession".equals(name)) return session;
                    if ("getRequestDispatcher".equals(name)) {
                        includedUrl[0] = (String) args[0];
                        return dispatcher;
                    }
                    if ("setCharacterEncoding".equals(name)) return null;
                    return null;
                }
        );
    }

    private HttpServletResponse createMockResponse(String[] redirectedUrl) {
        return (HttpServletResponse) Proxy.newProxyInstance(
                HttpServletResponse.class.getClassLoader(),
                new Class[]{HttpServletResponse.class},
                (proxy, m, args) -> {
                    String name = m.getName();
                    if ("sendRedirect".equals(name)) {
                        redirectedUrl[0] = (String) args[0];
                        return null;
                    }
                    return null;
                }
        );
    }

    @Test
    public void testEmptyUsernameRejected() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", "");
        params.put("password", "validPass123");
        String[] redirected = new String[1];
        String[] included = new String[1];

        HttpServletRequest req = createMockRequest("POST", "/login", sessionAttrs, requestAttrs, params, redirected, included);
        HttpServletResponse resp = createMockResponse(redirected);

        servlet.service(req, resp);

        assertNull(redirected[0], "Phải không redirect khi username rỗng");
        assertEquals("/login.jsp", included[0]);
        assertEquals("Vui lòng nhập tên đăng nhập.", requestAttrs.get("error"));
    }

    @Test
    public void testWhitespaceUsernameRejected() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", "    ");
        params.put("password", "validPass123");
        String[] redirected = new String[1];
        String[] included = new String[1];

        HttpServletRequest req = createMockRequest("POST", "/login", sessionAttrs, requestAttrs, params, redirected, included);
        HttpServletResponse resp = createMockResponse(redirected);

        servlet.service(req, resp);

        assertEquals("/login.jsp", included[0]);
        assertEquals("Vui lòng nhập tên đăng nhập.", requestAttrs.get("error"));
    }

    @Test
    public void testEmptyPasswordRejected() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", "admin");
        params.put("password", "");
        String[] redirected = new String[1];
        String[] included = new String[1];

        HttpServletRequest req = createMockRequest("POST", "/login", sessionAttrs, requestAttrs, params, redirected, included);
        HttpServletResponse resp = createMockResponse(redirected);

        servlet.service(req, resp);

        assertEquals("/login.jsp", included[0]);
        assertEquals("Vui lòng nhập mật khẩu.", requestAttrs.get("error"));
        assertEquals("admin", requestAttrs.get("username"), "Username phải được giữ lại");
        assertNull(requestAttrs.get("password"), "Mật khẩu tuyệt đối không được giữ lại");
    }

    @Test
    public void testInvalidCredentialsRejectedWithoutExposingUsername() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        Map<String, Object> requestAttrs = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", "non_existent_user_9999");
        params.put("password", "wrong_password_123");
        String[] redirected = new String[1];
        String[] included = new String[1];

        HttpServletRequest req = createMockRequest("POST", "/login", sessionAttrs, requestAttrs, params, redirected, included);
        HttpServletResponse resp = createMockResponse(redirected);

        servlet.service(req, resp);

        assertEquals("/login.jsp", included[0]);
        assertEquals("Sai tên đăng nhập hoặc mật khẩu.", requestAttrs.get("error"));
    }
}
