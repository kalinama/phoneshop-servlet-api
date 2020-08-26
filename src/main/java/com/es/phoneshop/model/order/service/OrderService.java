package com.es.phoneshop.model.order.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.enums.PaymentMethod;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order getOrder(Cart cart);
    void placeOrder(Order order);
    void setCustomerData(Order order, Map<String, String> parametersMap);
    List<PaymentMethod> getPaymentMethods();
}
