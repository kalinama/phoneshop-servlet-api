package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.enums.SortParameter;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);
    List<Product> findProducts(String query, SortParameter sortParameter, SortOrder sortOrder);
    void save(Product product);
    void delete(Long id);
}
