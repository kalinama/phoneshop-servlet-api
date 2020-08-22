package com.es.phoneshop.model.order.enums;

public enum PaymentMethod {
    CASH("Cash"), CREDIT_CARD("Credit card");

    PaymentMethod(String value){
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
