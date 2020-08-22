package com.es.phoneshop.model.item.dao;

import com.es.phoneshop.model.item.IdentifiedItem;
import com.es.phoneshop.model.item.ItemNotFoundExceptionFactory;
import com.es.phoneshop.model.item.enums.ItemType;

import java.util.ArrayList;
import java.util.List;

public abstract class ArrayListDao<T extends IdentifiedItem> implements Dao<T> {
    
    protected List<T> items;
    private long idMaxValue;

    private Class<T> type;

    public ArrayListDao(Class<T> type){
        items = new ArrayList<>();
        this.type = type;
    }

    @Override
    public T getById(Long id) {
            return items.stream()
                    .filter(item -> item.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> ItemNotFoundExceptionFactory.getException(
                            ItemType.of(type), id)
                    );
    }

    @Override
    public void save(T item) {
            if (item.getId()!=null) {
                items.stream()
                        .filter(existedItem -> existedItem.getId().equals(item.getId()))
                        .findAny()
                        .ifPresent(ItemWithSameId -> items.remove(ItemWithSameId));

                if (idMaxValue < item.getId())
                    idMaxValue = item.getId();
            } else
                item.setId(++idMaxValue);

            items.add(item);
    }

    @Override
    public void delete(Long id) {
            items.stream()
                    .filter(item -> item.getId().equals(id))
                    .findAny()
                    .ifPresent(ItemToRemove -> items.remove(ItemToRemove));
    }

}
