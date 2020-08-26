package com.es.phoneshop.model.cart.exception;

public class OutOfStockException extends Exception {
    private int availableStock;

    public OutOfStockException(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
