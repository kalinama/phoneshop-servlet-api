package com.es.phoneshop.model.order.exception;

import com.es.phoneshop.model.item.exception.ItemNotFoundException;

public class OrderNotFoundException extends ItemNotFoundException {

    public OrderNotFoundException(String errorMessage) { super(errorMessage); }
}
