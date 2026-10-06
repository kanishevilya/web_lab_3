<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Результат проверки</title>
    <style>
        body { font-family: sans-serif; max-width: 700px; margin: 20px auto; }
        table { border-collapse: collapse; }
        td, th { border: 1px solid #888; padding: 4px 12px; }
        .hit { color: green; } .miss { color: red; }
    </style>
</head>
<body>
<h1>Результат проверки</h1>
<table>
    <tr><th>X</th><th>Y</th><th>R</th><th>Время</th><th>Время работы, нс</th></tr>
    <tr>
        <td>${result.x}</td><td>${result.y}</td><td>${result.r}</td>
        <td>${result.time}</td><td>${result.nanos}</td>
    </tr>
</table>
<h2 class="${result.hit ? 'hit' : 'miss'}">
    <c:out value="${result.hit ? 'Точка попала в область' : 'Точка не попала в область'}"/>
</h2>
<p><a href="${pageContext.request.contextPath}/controller">Новый запрос</a></p>
</body>
</html>