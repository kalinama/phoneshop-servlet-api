package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PriceShift {

    private BigDecimal price;
    private Currency currency;
    private LocalDate startDate;

    PriceShift(BigDecimal price, Currency currency, LocalDate startDate)
    {
        this.price = price;
        this.currency = currency;
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
