<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<h2>Ошибка ${requestScope['jakarta.servlet.error.status_code']}</h2>
<p><c:out value="${requestScope['jakarta.servlet.error.message']}"/></p>
<a href="${pageContext.request.contextPath}/controller">Вернуться к форме</a>
</body>
</html>