<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục</title>
</head>
<body>
    <h2>Thêm danh mục mới</h2>
    <form action="add" method="post" enctype="multipart/form-data">
        <div>
            <label>Tên danh mục:</label>
            <input type="text" name="name" required/>
        </div>
        <div>
            <label>Ảnh đại diện:</label>
            <input type="file" name="icon"/>
        </div>
        <br/>
        <button type="submit">Thêm</button>
        <button type="reset">Hủy</button>
    </form>
    <br/>
    <a href="list">Quay lại danh sách</a>
</body>
</html>