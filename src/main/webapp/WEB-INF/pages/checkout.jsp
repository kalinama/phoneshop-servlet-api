<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

 <jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
 <jsp:useBean id="now" class="java.util.Date" scope="request"/>
 <tags:master pageTitle="Checkout page">
 <p/>

  <c:if test="${not empty orderErrors}">
      <div class="error"> An error occurred! Order not placed. </div>
  </c:if>

 <tags:orderProductsTable/>
 <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
    <table>
        <tags:customerDataInputRow name="First Name" paramName="firstName"/>
        <tags:customerDataInputRow name="Last Name" paramName="lastName"/>
        <tr>
            <td> Phone <font color="red">*</font> </td>
            <td>
                <font size="3" color="grey"> +375-xx-xxx-xx-xx </font> </br>
                <input type="tel" pattern="\+375[\-]?[0-9]{2}[\-]?[0-9]{3}[\-]?[0-9]{2}[\-]?[0-9]{2}"
                    name="phone" value="${param['phone']}" placeholder="+375-44-456-89-01"/>
                <c:if test="${not empty orderErrors['phone']}">
                    <div class="error"> ${orderErrors['phone']} </div>
                </c:if>
            </td>
        </tr>
        <tr>
            <td> Delivery date <font color="red">*</font> </td>
            <fmt:formatDate type="time" value="${now}" pattern="yyyy-MM-dd" var="dateNow"/>
            <td>
                <input type="date" min="${dateNow}" name="deliveryDate"
                    value="${not empty param['deliveryDate'] ? param['deliveryDate'] : dateNow}">
                <c:if test="${not empty orderErrors['deliveryDate']}">
                    <div class="error"> ${orderErrors['deliveryDate']} </div>
                </c:if>
            </td>
        </tr>
        <tags:customerDataInputRow name="Delivery address" paramName="deliveryAddress"/>
        <tr>
            <td>Payment method<font color="red">*</font> </td>
            <td>
                <select name="paymentMethod">
                    <option disabled selected>Choose payment method</option>
                    <c:forEach var="method" items="${paymentMethods}" varStatus="loop">
                        <option value="${method}" ${param['paymentMethod'] == method ? 'selected' : ''}>${method.value}</option>
                    </c:forEach>
                </select>
                <c:if test="${not empty orderErrors['paymentMethod']}">
                    <div class="error"> ${orderErrors['paymentMethod']} </div>
                </c:if>
            </td>
        </tr>
    </table>
    </br>
    <button>Place order</button>
  </form>
 </tags:master>