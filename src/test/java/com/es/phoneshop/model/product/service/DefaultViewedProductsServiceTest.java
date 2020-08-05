package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ViewedProductsUnit;
import com.es.phoneshop.model.product.dao.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultViewedProductsServiceTest {
    @Mock
    private HttpSession session;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private DefaultViewedProductsService productService;

    private ViewedProductsUnit viewedProductsUnit = new ViewedProductsUnit();
    private Currency usd = Currency.getInstance("USD");
    private int initialSize;

    private final String VIEWED_PRODUCT_SESSION_ATTRIBUTE_TEST;

    public DefaultViewedProductsServiceTest() throws NoSuchFieldException, IllegalAccessException {
        Field field = DefaultViewedProductsService.class.getDeclaredField("VIEWED_PRODUCT_SESSION_ATTRIBUTE");
        field.setAccessible(true);
        VIEWED_PRODUCT_SESSION_ATTRIBUTE_TEST = (String) field.get(null);

    }
    @Before
    public void setup() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        viewedProductsUnit = new ViewedProductsUnit();
        List<Product> testProductList = viewedProductsUnit.getRecentlyViewedProductsList();
        testProductList.add(new Product(1L,"sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        testProductList.add(new Product(2L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        testProductList.add(new Product(3L,"sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));

        initialSize = testProductList.size();

        Constructor<DefaultViewedProductsService> constructor = DefaultViewedProductsService.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        productService = constructor.newInstance();
        MockitoAnnotations.initMocks(this);

        when(session.getId()).thenReturn(anyString());
        when(session.getAttribute(VIEWED_PRODUCT_SESSION_ATTRIBUTE_TEST)).thenReturn(viewedProductsUnit);
    }

    @Test
    public void initTestListCorrect(){
        assertTrue(initialSize < ViewedProductsUnit.MAX_SIZE_OF_LIST);
    }

    @Test
    public void getViewedProductsUnitForSameSessionsTest() {
        ViewedProductsUnit firstUnit = productService.getViewedProductsUnit(session);
        ViewedProductsUnit secondUnit = productService.getViewedProductsUnit(session);

        assertSame(firstUnit, secondUnit);
    }

    @Test
    public void getViewedProductsUnitForDifferentSessionsTest() {
        HttpSession otherSession = mock(HttpSession.class);
        when(otherSession.getId()).thenReturn(anyString());
        when(otherSession.getAttribute(VIEWED_PRODUCT_SESSION_ATTRIBUTE_TEST)).thenReturn(null);

        ViewedProductsUnit firstUnit = productService.getViewedProductsUnit(session);
        ViewedProductsUnit secondUnit = productService.getViewedProductsUnit(otherSession);

        assertNotSame(firstUnit, secondUnit);
    }

    @Test
    public void addNewProductToViewedList(){
        Product product = new Product();
        when(productDao.getProduct(anyLong())).thenReturn(product);
        productService.addProductToViewed(viewedProductsUnit, anyLong());

        assertEquals(viewedProductsUnit.getRecentlyViewedProductsList().get(0), product);
    }

    @Test
    public void addProductToViewedMaxSizeList(){
        when(productDao.getProduct(anyLong())).thenReturn(new Product(4L,"iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productService.addProductToViewed(viewedProductsUnit, anyLong());

        when(productDao.getProduct(anyLong())).thenReturn(new Product(5L,"iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productService.addProductToViewed(viewedProductsUnit, anyLong());

        assertEquals(viewedProductsUnit.getRecentlyViewedProductsList().size(),
                ViewedProductsUnit.MAX_SIZE_OF_LIST);
    }

    @Test
    public void addExistedProductToViewed(){ //added product such as first product in list (index = 0)
        checkOrderAfterAddingExistingProduct(0);
    }

    @Test
    public void addSecondExistedProductToViewed(){ //added product such as second product in list (index = 1)
        checkOrderAfterAddingExistingProduct(1);
    }

    @Test
    public void addThirdExistedProductToViewed(){ //added product such as third product in list (index = 2)
        checkOrderAfterAddingExistingProduct(2);
    }

    private void checkOrderAfterAddingExistingProduct(int position)
    {
        int initialSize = viewedProductsUnit.getRecentlyViewedProductsList().size();
        Product product = viewedProductsUnit.getRecentlyViewedProductsList().get(position);
        when(productDao.getProduct(anyLong())).thenReturn(product);
        productService.addProductToViewed(viewedProductsUnit, product.getId());

        assertEquals(initialSize, viewedProductsUnit.getRecentlyViewedProductsList().size());
        assertEquals(product, viewedProductsUnit.getRecentlyViewedProductsList().get(0));
    }

    @Test
    public void getViewedProductsTest(){
        when(productDao.getProduct(anyLong())).thenReturn(new Product());
        productService.addProductToViewed(viewedProductsUnit, anyLong());
        List<Product> result = productService.getViewedProducts(viewedProductsUnit);

        assertEquals(viewedProductsUnit.getRecentlyViewedProductsList().size(), ViewedProductsUnit.MAX_SIZE_OF_LIST);
        assertEquals(result.size(), ViewedProductsUnit.AMOUNT_OF_VIEWED_PRODUCTS);
    }

    @Test
    public void getViewedProductsWithoutLastTest(){
        Product product = new Product();
        when(productDao.getProduct(anyLong())).thenReturn(product);
        productService.addProductToViewed(viewedProductsUnit, anyLong());
        List<Product> result = productService.getViewedProductsWithoutLast(viewedProductsUnit);

        assertEquals(viewedProductsUnit.getRecentlyViewedProductsList().size(), ViewedProductsUnit.MAX_SIZE_OF_LIST);
        assertEquals(result.size(), ViewedProductsUnit.AMOUNT_OF_VIEWED_PRODUCTS);
        assertNotEquals(product, result.get(0));
    }
}
