package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.model.item.dao.ArrayListDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao extends ArrayListDao<Order> implements OrderDao{

    private ArrayListOrderDao() {
        super(Order.class);
    }

    private static class ArrayListOrderDaoHolder {
        static final ArrayListOrderDao HOLDER_INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return ArrayListOrderDaoHolder.HOLDER_INSTANCE;
    }

    @Override
    public Order getBySecureId(String secureId) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            return items.stream()
                    .filter(item -> item.getSecureId().equals(secureId))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(secureId));
        } finally {
            lock.readLock().unlock();
        }
    }

}
