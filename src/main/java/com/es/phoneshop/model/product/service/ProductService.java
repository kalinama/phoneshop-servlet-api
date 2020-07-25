package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.enums.SortParameter;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ViewedProductsUnit;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ProductService {
    Product getProduct(Long id);
    List<Product> findProducts(String query, SortParameter sortParameter, SortOrder sortOrder);
    void save(Product product);
    void delete(Long id);
    ViewedProductsUnit getViewedProductsUnit(HttpSession httpSession);
    void addProductToViewed(ViewedProductsUnit viewedProductsUnit, Long productId);
}
