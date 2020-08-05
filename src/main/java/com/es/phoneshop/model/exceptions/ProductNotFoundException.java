package com.es.phoneshop.model.exceptions;

public class ProductNotFoundException extends RuntimeException{
    private long id;

    public ProductNotFoundException(String errorMessage) { super(errorMessage); }
    ProductNotFoundException(long id) { this.id = id; }

    public long getId() {
        return id;
    }
}
