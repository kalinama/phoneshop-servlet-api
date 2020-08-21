package com.es.phoneshop.model.item.exception;

public class ItemNotFoundException extends RuntimeException {
    private long id;

    public ItemNotFoundException(String errorMessage) { super(errorMessage); }
    //ItemNotFoundException(long id) { this.id = id; }

    public long getId() {
        return id;
    }
}
