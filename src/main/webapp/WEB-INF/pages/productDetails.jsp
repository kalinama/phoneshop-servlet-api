<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td><img src="${product.imageUrl}"></td>
      </tr>
      <tr>
        <td>Description</td>
        <td>${product.description}</td>
      </tr>
      <tr>
        <td>Price</td>
        <td><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/></td>
      </tr>
    </thead>
  </table>
</tags:master>