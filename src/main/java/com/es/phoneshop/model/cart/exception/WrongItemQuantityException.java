package com.es.phoneshop.model.cart.exception;

public class WrongItemQuantityException extends RuntimeException {

    public WrongItemQuantityException(String message){
        super(message);
    }

    public WrongItemQuantityException(){ }
}
