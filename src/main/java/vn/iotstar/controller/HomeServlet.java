package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

import java.io.IOException;

@WebServlet(urlPatterns = {"", "/"})
public class HomeServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        var list = productService.findLatest(10);
        System.out.println(">>> HomeServlet.doGet called! list size = " + (list != null ? list.size() : "null"));
        req.setAttribute(
                "products",
                list);

        req.getRequestDispatcher(
                "/home.jsp")
                .include(req, resp);
        System.out.println(">>> HomeServlet.doGet finished!");
    }
}
