package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ViewedProductsUnit;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface ViewedProductsService {
    ViewedProductsUnit getViewedProductsUnit(HttpSession httpSession);
    void addProductToViewed(ViewedProductsUnit viewedProductsUnit, Long productId);
    List<Product> getViewedProducts(ViewedProductsUnit viewedProductsUnit);
    List<Product> getViewedProductsWithoutLast(ViewedProductsUnit viewedProductsUnit);
}
