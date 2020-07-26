package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultViewedProductsService implements ViewedProductsService {

    private static final String VIEWED_PRODUCT_SESSION_ATTRIBUTE = DefaultViewedProductsService.class.getName() + ".viewedProduct";

    private ProductDao productDao;

    private DefaultViewedProductsService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static class DefaultProductServiceHolder {
        public static final DefaultViewedProductsService HOLDER_INSTANCE = new DefaultViewedProductsService();
    }

    public static DefaultViewedProductsService getInstance() {
        return DefaultViewedProductsService.DefaultProductServiceHolder.HOLDER_INSTANCE;
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
            }
            viewedProducts.add(0, product);
            viewedProducts.removeIf(productToRemove -> viewedProducts.indexOf(productToRemove)
                        == ViewedProductsUnit.MAX_SIZE_OF_LIST);
        }
    }

    public List<Product> getViewedProducts(ViewedProductsUnit viewedProductsUnit) {
        List<Product> recentlyViewedProductsList = viewedProductsUnit.getRecentlyViewedProductsList();
        return recentlyViewedProductsList.stream()
                .filter(product -> recentlyViewedProductsList.indexOf(product)
                        != ViewedProductsUnit.AMOUNT_OF_VIEWED_PRODUCTS)
                .collect(Collectors.toList());
    }

    public List<Product> getViewedProductsWithoutLast(ViewedProductsUnit viewedProductsUnit) {
        List<Product> recentlyViewedProductsList = viewedProductsUnit.getRecentlyViewedProductsList();
        return recentlyViewedProductsList.stream()
                .filter(product -> recentlyViewedProductsList.indexOf(product)!= 0)
                .collect(Collectors.toList());
    }

}
