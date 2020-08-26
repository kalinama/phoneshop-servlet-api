<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="paramName" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>
<%@ attribute name="isRequired"  %>


 <tr>
    <td>${name} ${isRequired == 'false' ? '' : '<span style="color: red; ">*</span>'} </td>
    <td>
        <input name=${paramName} value="${param[paramName]}">
        <c:if test="${not empty errors[paramName]}">
            <div class="error"> ${errors[paramName]} </div>
        </c:if>
    </td>
</tr>
