package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.model.item.dao.Dao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;

public interface OrderDao extends Dao<Order> {

    Order getBySecureId(String secureId) throws OrderNotFoundException;
}
