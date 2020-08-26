package com.es.phoneshop.model.product.exception;

import com.es.phoneshop.model.item.exception.ItemNotFoundException;

public class ProductNotFoundException extends ItemNotFoundException {

    public ProductNotFoundException(String errorMessage) { super(errorMessage); }

}
