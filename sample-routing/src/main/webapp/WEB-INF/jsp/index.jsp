<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title></title>
    </head>

    <body>
        <c:if test="${not empty countries}">

            <table border=1 cellpadding=5>
                <tr>
                    <th>Country</th>
                    <th>Currencies</th>
                </tr>

                <c:forEach var="country" items="${countries}">
                    <tr>
                        <td>${country.countryName}</td>
                        <td>${country.currenciesNames}</td>
                    </tr>
                </c:forEach>

            </table>

        </c:if>
    </body>
</html>
