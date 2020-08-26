package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.item.IdentifiedItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Cart extends IdentifiedItem {

    private List<CartItem> items;
    private int totalQuantity;
    private BigDecimal totalCost;
    private Currency currency = Currency.getInstance("USD");

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Cart{" + items +
                '}';
    }
}
