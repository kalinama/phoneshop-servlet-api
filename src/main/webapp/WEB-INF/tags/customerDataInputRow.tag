<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="paramName" required="true" %>

 <tr>
    <td>${name}<font color="red">*</font> </td>
    <td>
        <input name=${paramName} value="${param[paramName]}">
        <c:if test="${not empty orderErrors[paramName]}">
            <div class="error"> ${orderErrors[paramName]} </div>
        </c:if>
    </td>
</tr>
