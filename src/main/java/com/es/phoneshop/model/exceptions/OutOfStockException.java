package com.es.phoneshop.model.exceptions;

public class OutOfStockException extends Exception {
    private int availableStock;

    public OutOfStockException(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
