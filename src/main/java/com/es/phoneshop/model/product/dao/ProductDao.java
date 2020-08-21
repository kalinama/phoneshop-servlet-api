package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.item.dao.Dao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.enums.SortParameter;

import java.util.List;

public interface ProductDao extends Dao<Product> {
    List<Product> findProducts(String query, SortParameter sortParameter, SortOrder sortOrder);
}
