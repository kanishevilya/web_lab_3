<%--
  Created by IntelliJ IDEA.
  User: Liebert
  Date: 06.10.2026
  Time: 02:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Лабораторная работа №3</title>
    <style>
        body { font-family: sans-serif; max-width: 900px; margin: 20px auto; padding: 0 10px; }
        header { background: #e8eefc; padding: 10px 14px; border-radius: 6px; margin-bottom: 16px; }
        .row { margin: 10px 0; }
        .err { color: #c00; margin-left: 10px; }
        canvas { border: 1px solid #bbb; cursor: crosshair; max-width: 100%; }
        table { border-collapse: collapse; margin-top: 16px; }
        td, th { border: 1px solid #888; padding: 4px 10px; }
    </style>
</head>
<body>
<header>
    <b>ФИО:</b> Иванов Иван Иванович &nbsp;
    <b>Группа:</b> P3xxx &nbsp;
    <b>Вариант:</b> 469987
</header>

<form id="form" action="${pageContext.request.contextPath}/controller" method="post">
    <div class="row">X (-3 ... 3):
        <input type="text" name="x" id="x" autocomplete="off" placeholder="например, 1.5">
    </div>
    <div class="row">Y:
        <c:forEach var="v" items="${['-2','-1.5','-1','-0.5','0','0.5','1','1.5','2']}">
            <label><input type="radio" name="y" value="${v}"> ${v}</label>
        </c:forEach>
    </div>
    <div class="row">R:
        <select name="r" id="r">
            <option value="">—</option>
            <option value="1">1</option><option value="1.5">1.5</option>
            <option value="2">2</option><option value="2.5">2.5</option>
            <option value="3">3</option>
        </select>
    </div>
    <button type="submit">Проверить</button>
    <span id="error" class="err"></span>
</form>

<p>Клик по графику отправляет точку на проверку (нужно выбрать R).</p>
<canvas id="plot" width="420" height="420"></canvas>

<table>
    <thead>
    <tr><th>X</th><th>Y</th><th>R</th><th>Попадание</th><th>Время</th><th>нс</th></tr>
    </thead>
    <tbody>
    <c:forEach var="res" items="${applicationScope.results}">
        <tr>
            <td>${res.x}</td><td>${res.y}</td><td>${res.r}</td>
            <td>${res.hit ? 'Да' : 'Нет'}</td><td>${res.time}</td><td>${res.nanos}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<script>
    const POINTS = [
        <c:forEach var="res" items="${applicationScope.results}" varStatus="s">
        {x: ${res.x}, y: ${res.y}, r: ${res.r}, hit: ${res.hit}}<c:if test="${!s.last}">,</c:if>
        </c:forEach>
    ];
    const CONTROLLER_URL = "${pageContext.request.contextPath}/controller";
</script>
<script src="${pageContext.request.contextPath}/script.js"></script>
</body>
</html>
