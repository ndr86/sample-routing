<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title></title>
    </head>

    <body>
        <c:if test="${not empty lists}">

            <table border=1 cellpadding=5>
                <tr>
                    <th>Country</th>
                    <th>Concurrencies</th>
                </tr>

                <c:forEach var="listValue" items="${lists}">
                    <tr>
                        <td>${listValue}</td>
                        <td>${listValue}</td>
                    </tr>
                </c:forEach>

            </table>

        </c:if>
    </body>
</html>
