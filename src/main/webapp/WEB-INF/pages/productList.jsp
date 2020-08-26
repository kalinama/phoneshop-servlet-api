<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

 <jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
 <tags:master pageTitle="Product List">

 <p> Welcome to Expert-Soft training! </p>

 <form action="${pageContext.servletContext.contextPath}/advanced-search">
     <input type="submit" value="Advanced search" />
 </form>

 <form>
     <input name="query" value="${param.query}" />
     <button>Search</button>
 </form>

 <c:if test="${not empty param.message}">
     <div class="successfully"> ${param.message} </div>
 </c:if>
 <c:if test="${not empty param.wrongQuantityError}">
     <div class="error"> An error occurred! Product not added to cart. </div>
 </c:if>
 <p/>
 <table>
     <thead>
         <tr>
             <td>Image</td>
             <td>Description
                 <tags:sortLink sort="description" order="asc"/>
                 <tags:sortLink sort="description" order="desc"/>
             </td>
             <td class="price">Price
                 <tags:sortLink sort="price" order="asc"/>
                 <tags:sortLink sort="price" order="desc"/>
             </td>
             <td class="quantity">Quantity</td>
             <td></td>
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
             <form method="post" id="add" action="${pageContext.servletContext.contextPath}/cart/add-product/${product.id}">
                 <td>
                    <input class="quantity"  name="quantity"
                    value="${not empty param.wrongQuantityError and param.productId eq product.id ? param.quantity : 1}" />
                    <input type="hidden" name="pageCode" value="PLP" />

                    <c:set var="importantParams" value="${fn:split('sort,order,query', ',')}" />
                    <tags:hiddenParameters parameters="${importantParams}"/>

                    <c:if test="${not empty param.wrongQuantityError and param.productId eq product.id}">
                        <div class="error"> ${param.wrongQuantityError} </div>
                    </c:if>
                 </td>
                 <td><button>Add</button></td>
             </form>
         </tr>
     </c:forEach>
 </table>

 <tags:recentlyViewedProducts />
 </tags:master>



 <%--
    <c:forEach var="paramWithSameKeyEntry" items="${paramValues}">
        <c:forEach var="parameterValue" items="${paramValues[paramWithSameKeyEntry.key]}">
            <input type="hidden" name="${paramWithSameKeyEntry.key}" value="${parameterValue}" />
        </c:forEach>
    </c:forEach>
 --%>

 <%--
   ${requestScope['javax.servlet.forward.request_uri']}
 --%>