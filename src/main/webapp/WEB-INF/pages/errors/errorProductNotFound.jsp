<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product Not Found">
  <h1> Product with id ${requestScope['javax.servlet.error.message']} not found! <h1>
</tags:master>


<%--
<%@ page import="com.es.phoneshop.model.product.ProductNotFoundException" %>
<%@ page isErrorPage="true" %>
<%= ((ProductNotFoundException) exception).getId()%>
--%>