package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private List<Product> testProductList;
    private Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        testProductList = new ArrayList<>();
        testProductList.add(new Product(1L,"sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        testProductList.add(new Product(2L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        testProductList.add(new Product(3L,"sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        testProductList.add(new Product(4L,"iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        testProductList.add(new Product(5L,"iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao = new ArrayListProductDao(testProductList);
    }

    @Test
    public void testSetUpDao() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProductByExistedId() throws ProductNotFoundException {
        Product testProduct = testProductList.get(0);
        Product result = productDao.getProduct(testProduct.getId());

        assertEquals(testProduct, result);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductByNotExistedId() throws ProductNotFoundException {
        long notExistedId = 155;
        productDao.getProduct(notExistedId);
    }

    @Test
    public void testFindProductsWithSpecialConditions()  { // non null price and stock level > 0
        List<Product> result = productDao.findProducts();

        assertArrayEquals(testProductList.toArray(), result.toArray());
    }

    @Test
    public void testSaveProductWithoutId() throws ProductNotFoundException {
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);

        assertNotNull(product.getId());
        assertEquals(product, productDao.getProduct(product.getId()));
        assertEquals(testProductList.size()+1, productDao.findProducts().size());
    }

    @Test
    public void testSaveProductWithExistedId() throws ProductNotFoundException {
        long existedId = testProductList.get(0).getId();
        Product product = new Product(existedId, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDao.save(product);

        assertEquals(product, productDao.getProduct(existedId));
        assertEquals(testProductList.size(), productDao.findProducts().size());
    }

    @Test
    public void testSaveProductWithNotExistedId() throws ProductNotFoundException {
        long notExistedId = 199;
        Product product = new Product(notExistedId, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDao.save(product);

        assertEquals(product, productDao.getProduct(notExistedId));
        assertEquals(testProductList.size()+1, productDao.findProducts().size());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProductById() throws ProductNotFoundException {
        long idOfRemovableProduct = testProductList.get(0).getId();
        productDao.delete(idOfRemovableProduct);
        productDao.getProduct(idOfRemovableProduct);
    }

}
