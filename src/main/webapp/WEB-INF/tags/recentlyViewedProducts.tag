<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="viewedProducts" type="java.util.ArrayList" scope="request"/>

 <c:if test="${not empty viewedProducts}">
    <p> <strong> <u> <font size="4" color="dimgray"> Recently viewed: </font> </u> </strong> </p>
 </c:if>
 <table class="viewed-products-table">
     <c:forEach var="viewedProduct" items="${viewedProducts}">
         <td class="viewed-products-td">
             <img class="product-tile" src="${viewedProduct.imageUrl}">
             <p>
                 <a href="${pageContext.servletContext.contextPath}/products/${viewedProduct.id}">
                     ${viewedProduct.description}
                 </a>
             </p>
              <p>
                 <fmt:formatNumber value="${viewedProduct.price}" type="currency" currencySymbol="${viewedProduct.currency.symbol}"/>
              </p>
         </td>
     </c:forEach>
 </table>
