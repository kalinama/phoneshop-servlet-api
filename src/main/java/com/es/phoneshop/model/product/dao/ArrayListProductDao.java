package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.item.dao.ArrayListDao;
import com.es.phoneshop.model.product.AdvancedProductDescription;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.enums.SortParameter;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao extends ArrayListDao<Product> implements ProductDao {

    private static final String SEPARATOR = " ";

    private ArrayListProductDao() {
        super(Product.class);
    }

    private static class ArrayListProductDaoHolder {
        static final ArrayListProductDao HOLDER_INSTANCE = new ArrayListProductDao();
    }

    public static ArrayListProductDao getInstance() {
        return ArrayListProductDaoHolder.HOLDER_INSTANCE;
    }

    @Override
    public List<Product> findProducts(String query, SortParameter sortParameter, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparator = getComparatorForProductList(query,sortParameter,sortOrder);
            return items.stream()
                    .filter(product -> query==null || query.isEmpty()
                            || isProductMatchedQuery(product, query.toLowerCase()))
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
                ? getQueryMatchRate(product, query.toLowerCase()) : 0))
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
       return Arrays.stream(product.getDescription().toLowerCase().split(SEPARATOR))
                .anyMatch(wordFromDescription-> Arrays.stream(query.split(SEPARATOR))
                        .anyMatch(wordFromDescription::contains));

    }

    private double getQueryMatchRate(Product product, String query){
        long quantityOfWordInDescription = product.getDescription().split(SEPARATOR).length;

        long quantityOfCompleteMatchedWords = Arrays.stream(query.split(SEPARATOR))
                .filter(wordFromQuery -> Arrays.asList(product.getDescription()
                        .toLowerCase().split(SEPARATOR)).contains(wordFromQuery))
                .count();

        long quantityOfPartialMatchedWords =  Arrays.stream(query.split(SEPARATOR))
                .filter(wordFromQuery -> product.getDescription()
                        .toLowerCase().contains(wordFromQuery))
                .count();

        return  quantityOfCompleteMatchedWords
                + (double) quantityOfPartialMatchedWords / quantityOfWordInDescription;
    }

    @Override
    public List<Product> findProducts(AdvancedProductDescription advancedProductDescription) {
        lock.readLock().lock();
        try {
            String code = advancedProductDescription.getCode();
            BigDecimal priceMax =advancedProductDescription.getPriceMax();
            BigDecimal priceMin = advancedProductDescription.getPriceMin();
            Integer stockMin = advancedProductDescription.getStockMin();

            return items.stream()
                    .filter(product -> code.isEmpty() || product.getCode().equals(code))
                    .filter(product -> priceMax == null || product.getPrice().compareTo(priceMax) <=0)
                    .filter(product -> priceMin == null || product.getPrice().compareTo(priceMin) > 0 )
                    .filter(product -> stockMin == null || product.getStock() >= stockMin)
                    .collect(Collectors.toList());
        }
        finally {
            lock.readLock().unlock();
        }
    }
}
