package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.enums.SortParameter;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public class DefaultProductService implements ProductService {

    private static final String VIEWED_PRODUCT_SESSION_ATTRIBUTE = DefaultProductService.class.getName() + ".viewedProduct";

    private ProductDao productDao;

    private DefaultProductService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static class DefaultProductServiceHolder {
        public static final DefaultProductService HOLDER_INSTANCE = new DefaultProductService();
    }

    public static DefaultProductService getInstance() {
        return DefaultProductService.DefaultProductServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public Product getProduct(Long id){
        return productDao.getProduct(id);
    }

    @Override
    public List<Product> findProducts(String query, SortParameter sortParameter, SortOrder sortOrder){
        return productDao.findProducts(query, sortParameter, sortOrder);
    }

    @Override
    public void save(Product product){
        productDao.save(product);
    }

    @Override
    public void delete(Long id){
        productDao.delete(id);
    }

    @Override
    public ViewedProductsUnit getViewedProductsUnit(HttpSession httpSession) {
        final Object lock = httpSession.getId().intern();
        synchronized (lock) {
            ViewedProductsUnit viewedProductsUnit = (ViewedProductsUnit) httpSession
                    .getAttribute(VIEWED_PRODUCT_SESSION_ATTRIBUTE);

            if (viewedProductsUnit == null)
                httpSession.setAttribute(VIEWED_PRODUCT_SESSION_ATTRIBUTE,
                        viewedProductsUnit = new ViewedProductsUnit());

            return viewedProductsUnit;
        }
    }

    @Override
    public void addProductToViewed(ViewedProductsUnit viewedProductsUnit, Long productId){
        synchronized (viewedProductsUnit) {
            Product product = productDao.getProduct(productId);
            List<Product> viewedProducts = viewedProductsUnit.getRecentlyViewedProductsList();
            Optional<Product> existedProductInViewed = viewedProducts.stream()
                    .filter(product::equals)
                    .findAny();

            if (existedProductInViewed.isPresent()) {
                int index = viewedProducts.indexOf(existedProductInViewed.get());
                if (index == 0) return;
                viewedProducts.remove(index);
                viewedProducts.add(0, existedProductInViewed.get());
            } else {
                viewedProducts.add(0, product);
                viewedProducts.removeIf(p -> viewedProducts.indexOf(p)
                        == ViewedProductsUnit.AMOUNT_OF_VIEWED_PRODUCTS + 1);
            }
        }

    }
}
