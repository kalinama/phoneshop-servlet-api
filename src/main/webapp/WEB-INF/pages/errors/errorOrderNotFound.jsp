<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

 <tags:master pageTitle="Order Not Found">
   <h1> Order with id ${pageContext.exception.message} not found! <h1>
 </tags:master>
