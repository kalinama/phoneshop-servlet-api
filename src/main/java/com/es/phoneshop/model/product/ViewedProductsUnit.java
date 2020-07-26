package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewedProductsUnit {

    public static final int AMOUNT_OF_VIEWED_PRODUCTS = 3;
    public static final int MAX_SIZE_OF_LIST = AMOUNT_OF_VIEWED_PRODUCTS + 1;

    private List<Product> recentlyViewedProducts;

    public ViewedProductsUnit() {
        this.recentlyViewedProducts = new ArrayList<>();
    }

    public List<Product> getRecentlyViewedProductsList() {
        return recentlyViewedProducts;
    }

}
