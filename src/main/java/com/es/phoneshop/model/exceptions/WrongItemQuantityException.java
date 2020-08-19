package com.es.phoneshop.model.exceptions;

public class WrongItemQuantityException extends RuntimeException {

    public WrongItemQuantityException(String message){
        super(message);
    }

    public WrongItemQuantityException(){ }
}
