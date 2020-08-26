package com.es.phoneshop.model.product;

import java.math.BigDecimal;

public class AdvancedProductDescription {

    private String code;
    private BigDecimal priceMax;
    private BigDecimal priceMin;
    private Integer stockMin;

    public AdvancedProductDescription(String code, BigDecimal priceMax, BigDecimal priceMin, Integer stockMin) {
        this.code = code;
        this.priceMax = priceMax;
        this.priceMin = priceMin;
        this.stockMin = stockMin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(BigDecimal priceMax) {
        this.priceMax = priceMax;
    }

    public BigDecimal getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(BigDecimal priceMin) {
        this.priceMin = priceMin;
    }

    public Integer getStockMin() {
        return stockMin;
    }

    public void setStockMin(Integer stockMin) {
        this.stockMin = stockMin;
    }
}
