package com.es.phoneshop.model.order.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.dao.ArrayListOrderDao;
import com.es.phoneshop.model.order.dao.OrderDao;
import com.es.phoneshop.model.order.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;

public class DefaultOrderService implements OrderService {

    private OrderDao orderDao;

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    private static class DefaultOrderServiceHolder {
        static final DefaultOrderService HOLDER_INSTANCE = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) {
        synchronized (cart) {
            Order order = new Order();
            order.setDeliveryCosts(calculateDeliveryCosts());
            order.setSubtotal(cart.getTotalCost());
            order.setTotalQuantity(cart.getTotalQuantity());
            order.setItems(cart.getItems().stream().map(CartItem::clone).collect(Collectors.toList()));
            order.setTotalCost(order.getDeliveryCosts().add(order.getSubtotal()));
            return order;
        }
    }

    private BigDecimal calculateDeliveryCosts(){
        return new BigDecimal(10);
    }

    @Override
    public void placeOrder(Order order) {
        synchronized (order) {
            order.setSecureId(UUID.randomUUID().toString());
            orderDao.save(order);
        }
    }

    @Override
    public void setCustomerData(Order order, Map<String, String> parametersMap) {
        synchronized (order) {
            order.setFirstName(parametersMap.get(FIRST_NAME));
            order.setLastName(parametersMap.get(LAST_NAME));
            order.setPhone(parametersMap.get(PHONE));
            order.setDeliveryDate(LocalDate.parse(parametersMap.get(DELIVERY_DATE)));
            order.setDeliveryAddress(parametersMap.get(DELIVERY_ADDRESS));
            order.setPaymentMethod(PaymentMethod.valueOf(parametersMap.get(PAYMENT_METHOD)));
        }
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }
}
