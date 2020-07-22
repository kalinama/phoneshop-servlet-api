package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException{
    private long id;

    ProductNotFoundException(String errorMessage) { super(errorMessage); }
    ProductNotFoundException(long id) { this.id = id; }

    public long getId() {
        return id;
    }
}
