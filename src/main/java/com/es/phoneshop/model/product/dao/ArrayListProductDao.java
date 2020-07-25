package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.enums.SortParameter;
import com.es.phoneshop.model.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static final String SEPARATOR = " ";

    private List<Product> products;
    private long idMaxValue;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private ArrayListProductDao() { products = new ArrayList<>();}

    public static class ArrayListProductDaoHolder {
        public static final ArrayListProductDao HOLDER_INSTANCE = new ArrayListProductDao();
    }

    public static ArrayListProductDao getInstance() {
        return ArrayListProductDaoHolder.HOLDER_INSTANCE;
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> product.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id.toString()));
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortParameter sortParameter, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparator = getComparatorForProductList(query,sortParameter,sortOrder);
            return products.stream()
                    .filter(product -> query==null || query.isEmpty() || isProductMatchedQuery(product, query))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
        finally {
            lock.readLock().unlock();
        }
    }

    private Comparator<Product> getComparatorForProductList(String query, SortParameter sortParameter, SortOrder sortOrder){
        Comparator<Product> comparator = Comparator.comparing((Product product) -> ( query != null && !query.isEmpty()
                ? getQueryMatchRate(product, query) : 0))
                .reversed();

        if(sortParameter == SortParameter.description)
            comparator = Comparator.comparing(Product::getDescription);

        if (sortParameter == SortParameter.price)
            comparator = Comparator.comparing(Product::getPrice);

        if (sortOrder == SortOrder.desc)
            comparator = comparator.reversed();

        return comparator;
    }

    private boolean isProductMatchedQuery(Product product, String query) {
       return Arrays.stream(product.getDescription().split(SEPARATOR))
                .anyMatch(wordFromDescription-> Arrays.stream(query.split(SEPARATOR))
                        .anyMatch(wordFromDescription::contains));

    }

    private double getQueryMatchRate(Product product, String query){
        return (double) Arrays.stream(query.split(SEPARATOR))
                .filter(wordFromQuery -> product.getDescription().contains(wordFromQuery))
                .count()
                / product.getDescription().split(SEPARATOR).length;
    }

    @Override
    public void save(Product product) {
        lock.writeLock().lock();
        try {
            if (product.getId()!=null) {
                products.stream()
                        .filter(existedProduct -> existedProduct.getId().equals(product.getId()))
                        .findAny()
                        .ifPresent(productWithSameId -> products.remove(productWithSameId));

                if (idMaxValue < product.getId())
                    idMaxValue = product.getId();
            } else
                product.setId(++idMaxValue);

            products.add(product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        try {
           products.stream()
                   .filter(product -> product.getId().equals(id))
                   .findAny()
                   .ifPresent(productToRemove -> products.remove(productToRemove));
        }
        finally {
            lock.writeLock().unlock();
        }
    }

}
