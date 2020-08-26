package com.es.phoneshop.model.item.dao;

import com.es.phoneshop.model.item.IdentifiedItem;
import com.es.phoneshop.model.item.ItemNotFoundExceptionFactory;
import com.es.phoneshop.model.item.enums.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ArrayListDao<T extends IdentifiedItem> implements Dao<T> {
    
    protected List<T> items;
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    private long idMaxValue;
    private Class<T> type;

    public ArrayListDao(Class<T> type){
        items = new ArrayList<>();
        this.type = type;
    }

    @Override
    public T getById(Long id) {
        lock.readLock().lock();
        try {
            return items.stream()
                    .filter(item -> item.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> ItemNotFoundExceptionFactory.getException(
                            ItemType.of(type), id));
        } finally {
                lock.readLock().unlock();
            }
    }

    @Override
    public void save(T item) {
        lock.writeLock().lock();
        try {
            if (item.getId() != null) {
                items.stream()
                        .filter(existedItem -> existedItem.getId().equals(item.getId()))
                        .findAny()
                        .ifPresent(ItemWithSameId -> items.remove(ItemWithSameId));

                if (idMaxValue < item.getId())
                    idMaxValue = item.getId();
            } else
                item.setId(++idMaxValue);

            items.add(item);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        try {
            items.stream()
                    .filter(item -> item.getId().equals(id))
                    .findAny()
                    .ifPresent(ItemToRemove -> items.remove(ItemToRemove));
        } finally {
            lock.writeLock().unlock();
        }
    }
}
