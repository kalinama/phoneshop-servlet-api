<%@ attribute name="name" required="true" %>
<%@ attribute name="paramName" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>

 <tr>
    <td>${name}</td>
    <td>
       ${order[paramName]}
    </td>
 </tr>
