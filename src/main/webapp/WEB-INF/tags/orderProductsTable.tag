<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

 <table>
     <thead>
         <tr>
             <td>Image</td>
             <td>Description</td>
             <td class="price">Price</td>
             <td class="quantity">Quantity</td>
         </tr>
     </thead>
     <c:forEach var="orderItem" items="${order.items}" varStatus="loop">
         <tr>
             <td>
                 <img class="product-tile" src="${orderItem.product.imageUrl}">
             </td>
             <td>
                 <a href="${pageContext.servletContext.contextPath}/products/${orderItem.product.id}">
                     ${orderItem.product.description}
                 </a>
             </td>
             <td class="price">
                 <a href="${pageContext.servletContext.contextPath}/products/price-history/${orderItem.product.id}">
                     <fmt:formatNumber value="${orderItem.product.price}" type="currency"
                     currencySymbol="${orderItem.product.currency.symbol}"/>
                 </a>
             </td>
             <td class="quantity">
                <fmt:formatNumber value="${orderItem.quantity}"/>
             </td>
         </tr>
     </c:forEach>
     <tr bgcolor="lightYellow" class="total">
         <td class="price">
             Subtotal:
             <br> <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${order.currency.symbol}"/>
         </td>
         <td bgcolor="lightYellow" class="price">
             Delivery Costs:
             <br> <fmt:formatNumber value="${order.deliveryCosts}" type="currency" currencySymbol="${order.currency.symbol}"/>
         </td>
        <td bgcolor="lightSteelBlue" class="price">
            Total Cost:
            <br> <fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="${order.currency.symbol}"/>
        </td>
        <td bgcolor="lightSteelBlue" class="quantity">
            Total quantity: ${cart.totalQuantity}
        </td>
     </tr>
 </table>