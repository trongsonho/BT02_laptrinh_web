<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property='title'>MyShop</sitemesh:write></title>
    <sitemesh:write property='head'/>
    <style>
        * {
            box-sizing: border-box;
        }
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
            color: #333;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .site-main-content {
            flex: 1;
            width: 100%;
        }
        .site-footer {
            background-color: #343a40;
            color: #adb5bd;
            text-align: center;
            padding: 20px 0;
            margin-top: auto;
            font-size: 14px;
            border-top: 1px solid #495057;
        }
        .site-footer p {
            margin: 0;
        }
    </style>
</head>
<body>

    <!-- SiteMesh Managed Header -->
    <jsp:include page="/header.jsp" />

    <!-- SiteMesh Managed Main Content -->
    <main class="site-main-content">
        <sitemesh:write property='body'/>
    </main>

    <!-- SiteMesh Managed Footer -->
    <footer class="site-footer">
        <div>
            <p>&copy; 2026 MyShop. All rights reserved. | Quản lý hệ thống Bán hàng &amp; Người dùng</p>
        </div>
    </footer>

</body>
</html>
