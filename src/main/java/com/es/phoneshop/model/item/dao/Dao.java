package com.es.phoneshop.model.item.dao;

import com.es.phoneshop.model.item.exception.ItemNotFoundException;

public interface Dao<T> {
    T getById(Long id) throws ItemNotFoundException;
    void save(T item);
    void delete(Long id);
}
