package com.es.phoneshop.model.order.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.dao.OrderDao;
import com.es.phoneshop.model.order.enums.PaymentMethod;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.PAYMENT_METHOD;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private DefaultOrderService orderService;

    @Before
    public  void setup() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<DefaultOrderService> constructor = DefaultOrderService.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        orderService = constructor.newInstance();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getOrderTest(){
        Cart cart = new Cart();
        cart.setTotalCost(new BigDecimal(5));
        cart.setTotalQuantity(10);
        cart.setItems(Collections.singletonList(new CartItem(new Product(), 1)));
        Order order = orderService.getOrder(cart);

        assertNotNull(order.getDeliveryCosts());
        assertEquals(order.getSubtotal(), cart.getTotalCost());
        assertEquals(order.getTotalCost(), order.getDeliveryCosts().add(order.getSubtotal()));
        assertEquals(order.getTotalQuantity(), cart.getTotalQuantity());
        assertEquals(order.getItems().size(), cart.getItems().size());
        assertTrue(!order.getItems().equals(cart.getItems()));
    }

    @Test
    public void placeOrderTest(){
        Order order = new Order();
        orderService.placeOrder(order);

        assertNotNull(order.getSecureId());
        verify(orderDao).save(order);
    }
    
    @Test
    public void setCustomerDataTest(){
        Order order = new Order();
        String firstName = "Margarita";
        String lastName = "Ageenko";
        String phone = "+375447609415";
        String deliveryDate = "2020-08-22";
        String deliveryAddress = "yl. Minskaya";
        String paymentMethod = "CASH";
        
        Map<String,String> map = new HashMap<>();
        map.put(FIRST_NAME, firstName);
        map.put(LAST_NAME, lastName);
        map.put(PHONE, phone);
        map.put(DELIVERY_DATE, deliveryDate);
        map.put(DELIVERY_ADDRESS, deliveryAddress);
        map.put(PAYMENT_METHOD, paymentMethod);

        orderService.setCustomerData(order, map);

        assertEquals(order.getFirstName(), firstName);
        assertEquals(order.getLastName(), lastName);
        assertEquals(order.getPhone(), phone);
        assertEquals(order.getDeliveryDate(), LocalDate.parse(deliveryDate));
        assertEquals(order.getDeliveryAddress(), deliveryAddress);
        assertEquals(order.getPaymentMethod(), PaymentMethod.valueOf(paymentMethod));
    }

}
