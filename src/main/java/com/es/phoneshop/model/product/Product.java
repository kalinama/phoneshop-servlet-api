package com.es.phoneshop.model.product;

import com.es.phoneshop.model.item.IdentifiedItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class Product extends IdentifiedItem {
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceShift> priceHistory;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        super(id);
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        priceHistory = new ArrayList<>();
        priceHistory.add(new PriceShift(price, currency, LocalDate.now()));

    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        priceHistory = new ArrayList<>();
        priceHistory.add(new PriceShift(price, currency, LocalDate.now()));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        priceHistory.add(new PriceShift(price, currency, LocalDate.now()));
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PriceShift> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceShift> priceHistory) { this.priceHistory = priceHistory;}

    @Override
    public boolean equals(Object obj) {

        if (obj == this)
            return true;

        if (obj == null || obj.getClass() != this.getClass())
            return false;

        Product product = (Product) obj;

        return Objects.equals(super.getId(), product.getId())
                && Objects.equals(code, product.code)
                && Objects.equals(description, product.description)
                && Objects.equals(price, product.price)
                && Objects.equals(currency, product.currency)
                && Objects.equals(stock, product.stock)
                && Objects.equals(imageUrl, product.imageUrl);
    }

}
