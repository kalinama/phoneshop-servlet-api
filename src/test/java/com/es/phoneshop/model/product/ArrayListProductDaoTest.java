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
    private Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        productDao = new ArrayListProductDao(true);
    }

    @Test
    public void testInitListWithSampleProducts() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProductById() throws ProductNotFoundException {
        productDao.save(new Product(1L,"sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        Product product = productDao.getProduct(1L);

        assertEquals(1, (long) product.getId());
    }

    @Test
    public void testFindProducts()  {
        ProductDao productDaoRenew = new ArrayListProductDao(false);
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productList.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 54, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productList.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));

        for(Product product: productList)
            productDaoRenew.save(product);

        List<Product> checkedProductList = productDaoRenew.findProducts();

        assertArrayEquals(productList.toArray(), checkedProductList.toArray());
    }

    @Test
    public void testSaveProductWithoutId() throws ProductNotFoundException {
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        assertNotNull(product.getId());
        assertEquals(product, productDao.getProduct(product.getId()));

    }

    @Test
    public void testSaveProductWithExistedId() throws ProductNotFoundException {
        productDao = new ArrayListProductDao(false);
        productDao.save(new Product(51L,"nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));

        Product resultProduct = new Product(51L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDao.save(resultProduct);
        assertEquals(productDao.getProduct(51L), resultProduct);
    }

    @Test
    public void testSaveProductWithNotExistedId() throws ProductNotFoundException {
        ProductDao productDaoRenew = new ArrayListProductDao(false);
        Product resultProduct = new Product(13L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDaoRenew.save(resultProduct);
        assertEquals(productDaoRenew.getProduct(13L), resultProduct);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testDeleteProductById() throws ProductNotFoundException {
        productDao = new ArrayListProductDao(false);
        Product resultProduct = new Product(13L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDao.save(resultProduct);
        productDao.delete(13L);
        exception.expect(ProductNotFoundException.class);
        productDao.getProduct(13L);
    }
}
