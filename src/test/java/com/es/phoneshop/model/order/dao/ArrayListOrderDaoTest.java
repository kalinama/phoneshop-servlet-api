package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.model.item.dao.ArrayListDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class ArrayListOrderDaoTest {

    private OrderDao orderDao;
    private List<Order> testOrderList;
    private long maxId;

    @Before
    public void setup() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        testOrderList = new ArrayList<>();// non null price and stock level > 0

        for (int i = 0; i < 5; i++){
            Order order = new Order();
            order.setSecureId(UUID.randomUUID().toString());
            order.setId(i+1L);
            testOrderList.add(order);
        }

        Constructor<ArrayListOrderDao> constructor = ArrayListOrderDao.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        orderDao = constructor.newInstance();

        Field fieldProducts = ArrayListDao.class.getDeclaredField("items");
        fieldProducts.setAccessible(true);
        fieldProducts.set(orderDao, testOrderList);

        maxId = testOrderList.stream()
                .max(Comparator.comparing(Order::getId))
                .get()
                .getId();

        Field fieldIdMaxValue = ArrayListDao.class.getDeclaredField("idMaxValue");
        fieldIdMaxValue.setAccessible(true);
        fieldIdMaxValue.set(orderDao, maxId);
    }

    @Test
    public void testGetOrderByExistedSecureId() throws ProductNotFoundException {
        Order testOrder = testOrderList.get(0);
        Order result = orderDao.getBySecureId(testOrder.getSecureId());

        assertEquals(testOrder, result);
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderByNotExistedSecureId() throws ProductNotFoundException {
        String notExistedId = UUID.randomUUID().toString();
        orderDao.getBySecureId(notExistedId);
    }

    @Test
    public void testGetOrderByExistedId() throws OrderNotFoundException {
        Order testOrder = testOrderList.get(0);
        Order result = orderDao.getById(testOrder.getId());

        assertEquals(testOrder, result);
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderByNotExistedId() throws OrderNotFoundException {
        long notExistedId = maxId + 1;
        orderDao.getById(notExistedId);
    }

    @Test
    public void testSaveOrderWithoutId() throws OrderNotFoundException {
        Order order = new Order();
        int oldSize = testOrderList.size();
        orderDao.save(order);

        assertNotEquals(testOrderList.size(), oldSize);
        assertNotNull(order.getId());
        assertEquals(order, orderDao.getById(order.getId()));
    }

    @Test
    public void testSaveOrderWithExistedId() throws OrderNotFoundException {
        long existedId = testOrderList.get(0).getId();
        Order order = new Order();
        order.setId(existedId);
        int oldSize = testOrderList.size();
        orderDao.save(order);

        assertEquals(testOrderList.size(), oldSize);
        assertEquals(order, orderDao.getById(existedId));
    }

    @Test
    public void testSaveOrderWithNotExistedId() throws OrderNotFoundException {
        long notExistedId = maxId + 1;
        Order order = new Order();
        order.setId(notExistedId);
        int oldSize = testOrderList.size();
        orderDao.save(order);

        assertNotEquals(testOrderList.size(), oldSize);
        assertEquals(order, orderDao.getById(notExistedId));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testDeleteOrderById() throws OrderNotFoundException {
        long idOfRemovableOrder = testOrderList.get(0).getId();
        orderDao.delete(idOfRemovableOrder);
        orderDao.getById(idOfRemovableOrder);
    }

}
