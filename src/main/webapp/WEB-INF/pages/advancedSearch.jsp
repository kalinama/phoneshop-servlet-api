<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:master pageTitle="Advanced search page">
    <br>
    <c:if test="${not empty products}">
    <p class="successfully">Found ${products.size()} products</p>
    </c:if>
<form method="post">
    <table>
        <tr><tags:customerDataInputRow name="Product code" paramName="productCode" errors="${searchErrors}" isRequired="false"/></tr>
        <tr><tags:customerDataInputRow name="Min Price" paramName="minPrice" errors="${searchErrors}" isRequired="false"/></tr>
        <tr><tags:customerDataInputRow name="Max price" paramName="maxPrice" errors="${searchErrors}" isRequired="false"/></tr>
        <tr><tags:customerDataInputRow name="Min Stock" paramName="minStock" errors="${searchErrors}" isRequired="false"/></tr>

    </table>
    <br> <button>Search</button>
</form>
    <c:if test="${not empty products}">

    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description
            </td>
            <td class="price">Price

            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>

            </tr>
        </c:forEach>
    </table>
    </c:if>
</tags:master>
