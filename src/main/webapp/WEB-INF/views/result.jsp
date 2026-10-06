<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Результат проверки</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<h1>Результат проверки</h1>
<table>
    <tr>
        <th>X</th>
        <th>Y</th>
        <th>R</th>
        <th>Время</th>
        <th>Время работы, нс</th>
    </tr>
    <tr>
        <td>${result.x}</td>
        <td>${result.y}</td>
        <td>${result.r}</td>
        <td id="result-time" data-iso="${result.time}">${result.time}</td>
        <td>${result.nanos}</td>
    </tr>
</table>

<h2 class="${result.hit ? 'hit' : 'miss'}">
    <c:out value="${result.hit ? 'Точка попала в область' : 'Точка не попала в область'}"/>
</h2>

<p><a href="${pageContext.request.contextPath}/controller?r=${result.r}">Новый запрос</a></p>
<script>
    const cell = document.getElementById('result-time');
    const iso = cell.getAttribute('data-iso');
    if (iso) {
        const dateObj = new Date(iso);
        if (!isNaN(dateObj.getTime())) {
            cell.textContent = new Intl.DateTimeFormat('ru-RU', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            }).format(dateObj);
        }
    }
</script>
</body>
</html>