<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<a class="miniCart" href="${pageContext.servletContext.contextPath}/cart">
    Cart: ${cart.totalQuantity} items
    <c:if test="${cart.totalCost gt 0}">
        - <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${cart.currency.symbol}"/>
    </c:if>
</a>
