package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private List<Product> testProductList;
    private Currency usd = Currency.getInstance("USD");
    private long maxId;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        testProductList = new ArrayList<>();
        testProductList.add(new Product(1L,"sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        testProductList.add(new Product(2L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        testProductList.add(new Product(3L,"sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        testProductList.add(new Product(4L,"iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        testProductList.add(new Product(5L,"iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));

        Field instance = ArrayListProductDao.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        productDao = ArrayListProductDao.getInstance();
        testProductList.forEach(productDao::save);

        maxId = testProductList.stream()
                .max(Comparator.comparing(Product::getId))
                .get()
                .getId();
    }

    @Test
    public void testSetUpDao() {
        testProductList.forEach(product -> productDao.getProduct(product.getId()));
    }

    @Test
    public void testGetProductByExistedId() throws ProductNotFoundException {
        Product testProduct = testProductList.get(0);
        Product result = productDao.getProduct(testProduct.getId());

        assertEquals(testProduct, result);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductByNotExistedId() throws ProductNotFoundException {
        long notExistedId = maxId + 1;
        productDao.getProduct(notExistedId);
    }

    @Test
    public void testSaveProductWithoutId() throws ProductNotFoundException {
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        long amountOfTestProductInDao = testProductList.stream()
                .filter(testProduct -> productDao.getProduct(testProduct.getId()).equals(testProduct))
                .count();

        assertEquals(testProductList.size(), amountOfTestProductInDao);
        assertNotNull(product.getId());
        assertEquals(product, productDao.getProduct(product.getId()));
    }

    @Test
    public void testSaveProductWithExistedId() throws ProductNotFoundException {
        long existedId = testProductList.get(0).getId();
        Product product = new Product(existedId, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDao.save(product);

        assertEquals(product, productDao.getProduct(existedId));
    }

    @Test
    public void testSaveProductWithNotExistedId() throws ProductNotFoundException {
        long notExistedId = maxId + 1;
        Product product = new Product(notExistedId, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDao.save(product);
        long amountOfTestProductInDao = testProductList.stream()
                .filter(testProduct -> productDao.getProduct(testProduct.getId()).equals(testProduct))
                .count();

        assertEquals(testProductList.size(), amountOfTestProductInDao);
        assertEquals(product, productDao.getProduct(notExistedId));
    }

    @Test
    public void testFindProductsWithSpecialConditions()  { // non null price and stock level > 0
        List<Product> result = productDao.findProducts(null, null, null);

        assertArrayEquals(testProductList.toArray(), result.toArray());
    }


    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProductById() throws ProductNotFoundException {
        long idOfRemovableProduct = testProductList.get(0).getId();
        productDao.delete(idOfRemovableProduct);
        productDao.getProduct(idOfRemovableProduct);
    }

}
