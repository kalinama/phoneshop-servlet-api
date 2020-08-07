<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="parameters" required="true" type="java.lang.String[]" %>

 <c:forEach var="parameterName" items="${parameters}">
    <c:if test="${not empty param[parameterName]}">
        <input type="hidden" name="${parameterName}" value="${param[parameterName]}"/>
    </c:if>
 </c:forEach>