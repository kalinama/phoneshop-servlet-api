package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewedProductsUnit {

    public static final int AMOUNT_OF_VIEWED_PRODUCTS = 3;

    private List<Product> recentlyViewedProducts;

    public ViewedProductsUnit() {
        this.recentlyViewedProducts = new ArrayList<>();
    }

    public List<Product> getRecentlyViewedProductsList() {
        return recentlyViewedProducts;
    }

    public List<Product> getFixedAmountOfViewedProducts() {

        return recentlyViewedProducts.stream()
                .filter(product -> recentlyViewedProducts.indexOf(product)
                        != AMOUNT_OF_VIEWED_PRODUCTS)
                .collect(Collectors.toList());
    }

    public List<Product> getViewedProductsWithoutLast() {
        return recentlyViewedProducts.stream()
                .filter(product -> recentlyViewedProducts.indexOf(product)!=0)
                .collect(Collectors.toList());
    }

}
