<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

 <jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
 <tags:master pageTitle="Cart">
 <p/>
 <c:if test="${empty cart.items}">
     <h1> Your cart is empty. </h1>
 </c:if>

 <c:if test="${not empty param.message}">
     <div class="successfully"> ${param.message} </div>
 </c:if>
 <c:if test="${not empty wrongQuantityErrors}">
     <div class="error"> An error occurred! Cart not updated completely. </div>
 </c:if>
 <p/>

 <c:if test="${not empty cart.items}">
     <table>
         <thead>
             <tr>
                 <td>Image</td>
                 <td>Description</td>
                 <td class="price">Price</td>
                 <td class="quantity">Quantity</td>
                 <td/>
             </tr>
         </thead>
         <c:forEach var="cartItem" items="${cart.items}" varStatus="loop">
             <tr>
                 <td>
                     <img class="product-tile" src="${cartItem.product.imageUrl}">
                 </td>
                 <td>
                     <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
                         ${cartItem.product.description}
                     </a>
                 </td>
                 <td class="price">
                     <a href="${pageContext.servletContext.contextPath}/products/price-history/${cartItem.product.id}">
                         <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                         currencySymbol="${cartItem.product.currency.symbol}"/>
                     </a>
                 </td>
                 <td>
                    <fmt:formatNumber value="${cartItem.quantity}" var="quantityVar"/>
                    <input class="quantity" name="quantity"
                        value="${not empty wrongQuantityErrors[cartItem.product.id] ? paramValues.quantity[loop.index] : quantityVar}"
                        form="update"/>
                    <input type="hidden" name="productId" value="${cartItem.product.id}" form="update"/>
                    <c:if test="${not empty wrongQuantityErrors[cartItem.product.id]}">
                        <div class="error"> ${wrongQuantityErrors[cartItem.product.id]} </div>
                    </c:if>
                 </td>
                 <td>
                    <form method="post" action="${pageContext.servletContext.contextPath}/cart/delete-cart-item/${cartItem.product.id}">
                        <button>Delete from cart</button>
                    </form>
                  </td>
             </tr>
         </c:forEach>
         <tr bgcolor="#CCCFF">
            <td/>
            <td/>
            <td class="price">
                Total Cost:
                <br> <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${cart.currency.symbol}"/>
            </td>
            <td class="quantity">
                Total quantity: ${cart.totalQuantity}
            </td>
            <td>
                </form>
                <form action="${pageContext.servletContext.contextPath}/checkout">
                    <input type="submit" value="Proceed to Checkout" />
                </form>
            </td>
         </tr>
     </table>

      <form id="update" method="post" action="${pageContext.servletContext.contextPath}/cart">
          <button>Update cart</button>
      </form>

 </c:if>

 </tags:master>