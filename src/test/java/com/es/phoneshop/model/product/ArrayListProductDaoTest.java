package com.es.phoneshop.model.product;

import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.enums.SortParameter;
import com.es.phoneshop.model.exceptions.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private List<Product> testProductList;
    private Currency usd = Currency.getInstance("USD");
    private long maxId;

    @Before
    public void setup() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        testProductList = new ArrayList<>();// non null price and stock level > 0
        testProductList.add(new Product(1L,"sgs", "Samsung Galaxy", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        testProductList.add(new Product(2L,"sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        testProductList.add(new Product(3L,"iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        testProductList.add(new Product(4L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        testProductList.add(new Product(5L,"iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));

        maxId = testProductList.stream()
                .max(Comparator.comparing(Product::getId))
                .get()
                .getId();

        Constructor<ArrayListProductDao> constructor = ArrayListProductDao.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        productDao = constructor.newInstance();

        Field fieldProducts = ArrayListProductDao.class.getDeclaredField("products");
        fieldProducts.setAccessible(true);
        fieldProducts.set(productDao, testProductList);

        Field fieldIdMaxValue = ArrayListProductDao.class.getDeclaredField("idMaxValue");
        fieldIdMaxValue.setAccessible(true);
        fieldIdMaxValue.set(productDao, maxId);
        //testProductList.forEach(productDao::save);
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
        int oldSize = testProductList.size();
        productDao.save(product);

        assertNotEquals(testProductList.size(), oldSize);
        assertNotNull(product.getId());
        assertEquals(product, productDao.getProduct(product.getId()));
    }

    @Test
    public void testSaveProductWithExistedId() throws ProductNotFoundException {
        long existedId = testProductList.get(0).getId();
        Product product = new Product(existedId, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        int oldSize = testProductList.size();
        productDao.save(product);

        assertEquals(testProductList.size(), oldSize);
        assertEquals(product, productDao.getProduct(existedId));
    }

    @Test
    public void testSaveProductWithNotExistedId() throws ProductNotFoundException {
        long notExistedId = maxId + 1;
        Product product = new Product(notExistedId, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        int oldSize = testProductList.size();
        productDao.save(product);

        assertNotEquals(testProductList.size(), oldSize);
        assertEquals(product, productDao.getProduct(notExistedId));
    }

    @Test
    public void testFindProductsWithoutParameters()  { // non null price and stock level > 0
        List<Product> result = productDao.findProducts(null, null, null);

        assertArrayEquals(testProductList.toArray(), result.toArray());
    }

    @Test
    public void testFindProductsWithQuery()  {
        List<Product> result = productDao.findProducts("Samsung S", null, null);
        List<String> correctList = new ArrayList<>();
        correctList.add("Samsung Galaxy");
        correctList.add("Samsung Galaxy S III");
        correctList.add("Samsung Galaxy S II");

        assertEquals(correctList.get(0), result.get(0).getDescription());
        assertEquals(correctList.get(1), result.get(1).getDescription());
        assertEquals(correctList.get(2), result.get(2).getDescription());
    }

    @Test
    public void testFindProductsWithAscPriceSort()  {
        List<Product> result = productDao.findProducts(null, SortParameter.price, SortOrder.asc);
        testProductList = testProductList.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());

        assertArrayEquals(testProductList.toArray(), result.toArray());
    }

    @Test
    public void testFindProductsWithDescPriceSort()  {
        List<Product> result = productDao.findProducts(null, SortParameter.price, SortOrder.desc);
        testProductList = testProductList.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .collect(Collectors.toList());

        assertArrayEquals(testProductList.toArray(), result.toArray());
    }

    @Test
    public void testFindProductsWithAscDescriptionSort()  {
        List<Product> result = productDao.findProducts(null, SortParameter.description, SortOrder.asc);
        testProductList = testProductList.stream()
                .sorted(Comparator.comparing(Product::getDescription))
                .collect(Collectors.toList());

        assertArrayEquals(testProductList.toArray(), result.toArray());
    }

    @Test
    public void testFindProductsWithDescDescriptionSort()  {
        List<Product> result = productDao.findProducts(null, SortParameter.description, SortOrder.desc);
        testProductList = testProductList.stream()
                .sorted(Comparator.comparing(Product::getDescription).reversed())
                .collect(Collectors.toList());

        assertArrayEquals(testProductList.toArray(), result.toArray());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProductById() throws ProductNotFoundException {
        long idOfRemovableProduct = testProductList.get(0).getId();
        productDao.delete(idOfRemovableProduct);
        productDao.getProduct(idOfRemovableProduct);
    }

}
