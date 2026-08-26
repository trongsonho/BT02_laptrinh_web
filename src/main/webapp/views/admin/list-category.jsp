<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách danh mục</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;  /* Thay thế cho cellpadding */
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h2>Danh sách danh mục</h2>
    <a href="add">Thêm danh mục</a>
    <table> <!-- Không còn cellpadding -->
        <thead>
            <tr>
                <th>STT</th>
                <th>Hình ảnh</th>
                <th>Tên danh mục</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${categoryList}" var="cate" varStatus="STT">
                <tr>
                    <td>${STT.index + 1}</td>
                    <td>
                        <c:if test="${not empty cate.icon}">
                            <img src="${pageContext.request.contextPath}/image?fname=${cate.icon}" 
                                 width="100" height="100" alt="Image"/>
                        </c:if>
                    </td>
                    <td>${cate.name}</td>
                    <td>
                        <a href="edit?id=${cate.id}">Sửa</a> | 
                        <a href="delete?id=${cate.id}" onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>