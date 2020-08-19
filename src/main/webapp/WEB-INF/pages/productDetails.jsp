<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

 <jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
 <tags:master pageTitle="Product Details">

 <p/>
 <c:if test="${not empty param.message}">
     <div class="successfully"> ${param.message} </div>
 </c:if>
 <c:if test="${not empty param.wrongQuantityError}">
     <div class="error"> An error occurred! Product not added to cart. </div>
 </c:if>
 <p>
    <strong> <font size="5"> ${product.description} </font> </strong>
 </p>

 <table>
     <thead>
         <tr>
             <td>Image</td>
             <td><img src="${product.imageUrl}"></td>
         </tr>
         <tr>
             <td>Stock</td>
             <td class="stock">${product.stock}</td>
         </tr>
         <tr>
             <td>Price</td>
             <td class="price">
                 <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
                     <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                 </a>
             </td>
         </tr>
     </thead>
 </table>

 <p> Add to cart: Input quantity <p>
 <form method="post" action="${pageContext.servletContext.contextPath}/cart/add-product/${product.id}">
     <input class="quantity" name="quantity" value="${not empty param.wrongQuantityError ? param.quantity : 1}" />
     <input type="hidden" name="pageCode" value="PDP" />
     <button>Add</button>
 </form>
 <c:if test="${not empty param.wrongQuantityError}">
     <div class="error"> ${param.wrongQuantityError} </div>
 </c:if>

 <tags:recentlyViewedProducts />
 </tags:master>