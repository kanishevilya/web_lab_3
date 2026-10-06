<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Лабораторная работа №3</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<header class="page-header">
    <div>Канищев Илья</div>
    <div>Группа: P3208</div>
    <div>Вариант: 469987</div>
</header>

<main class="content-wrapper">
    <div class="graph-container">
        <canvas id="plot" width="460" height="460"></canvas>
    </div>

    <div class="form-container">
        <form id="form" action="${pageContext.request.contextPath}/controller" method="post">
            <div class="input-group">
                <label for="x">Координата X (-3 ... 3):</label>
                <input type="text" name="x" id="x" autocomplete="off" placeholder="например, 1.5">
            </div>

            <div class="input-group">
                <div>Координата Y:</div>
                <div class="radio-group">
                    <c:forEach var="v" items="${['-2','-1.5','-1','-0.5','0','0.5','1','1.5','2']}">
                        <label><input type="radio" name="y" value="${v}"> ${v}</label>
                    </c:forEach>
                </div>
            </div>

            <div class="input-group">
                <label for="r">Радиус R:</label>
                <select name="r" id="r">
                    <option value="">—</option>
                    <c:forEach var="v" items="${['1','1.5','2','2.5','3']}">
                        <option value="${v}" ${param.r == v ? 'selected' : ''}>${v}</option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="submit-btn">Проверить</button>
            <div id="error" class="error-message"></div>
        </form>
    </div>
</main>

<div class="table-container">
    <table>
        <thead>
        <tr>
            <th>X</th>
            <th>Y</th>
            <th>R</th>
            <th>Результат</th>
            <th>Время проверки</th>
            <th>Время работы, нс</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="res" items="${applicationScope.results}">
            <tr>
                <td>${res.x}</td>
                <td>${res.y}</td>
                <td>${res.r}</td>
                <td style="color: ${res.hit ? '#008800' : '#cc0000'}; font-weight: bold;">
                        ${res.hit ? 'Попадание' : 'Промах'}
                </td>
                <td class="time-cell" data-iso="${res.time}">${res.time}</td>
                <td>${res.nanos}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    const POINTS = [
        <c:forEach var="res" items="${applicationScope.results}" varStatus="s">
        {x: "${res.x}", y: "${res.y}", r: "${res.r}", hit: ${res.hit}}<c:if test="${!s.last}">,</c:if>
        </c:forEach>
    ];
    const CONTROLLER_URL = "${pageContext.request.contextPath}/controller";
</script>
<script src="${pageContext.request.contextPath}/script.js"></script>
</body>
</html>