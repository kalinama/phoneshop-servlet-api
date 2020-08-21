<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

 <jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
 <tags:master pageTitle="OrderOverview page">
 <h2> Your order is accepted.</h2>
 <tags:orderProductsTable/>
 <br>
    <table>
        <tags:customerDataRow name="First Name" paramName="firstName"/>
        <tags:customerDataRow name="Last Name" paramName="lastName"/>
        <tags:customerDataRow name="Phone" paramName="phone"/>
        <tags:customerDataRow name="Delivery date" paramName="deliveryDate"/>
        <tags:customerDataRow name="Delivery address" paramName="deliveryAddress"/>
        <tags:customerDataRow name="Payment method" paramName="paymentMethod"/>
   </table>
   </br>
 </tags:master>