package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.exceptions.WrongItemQuantityException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultCartServiceTest {
    @Mock
    private HttpSession session;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private DefaultCartService cartService;

    private Cart cart;
    private Currency usd = Currency.getInstance("USD");
    private final String CART_SESSION_ATTRIBUTE_TEST;

    public DefaultCartServiceTest() throws IllegalAccessException, NoSuchFieldException {
        Field field = DefaultCartService.class.getDeclaredField("CART_SESSION_ATTRIBUTE");
        field.setAccessible(true);
        CART_SESSION_ATTRIBUTE_TEST = (String) field.get(null);
    }

    @Before
    public void setup() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        cart = new Cart();
        Product testProduct = new Product(1L,"sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        cart.getItems().add(new CartItem(testProduct, 1));

        Constructor<DefaultCartService> constructor = DefaultCartService.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        cartService = constructor.newInstance();

        MockitoAnnotations.initMocks(this);

        when(session.getId()).thenReturn(anyString());
        when(session.getAttribute(CART_SESSION_ATTRIBUTE_TEST)).thenReturn(cart);
    }

    @Test
    public void getCartForSameSessionsTest() {
        Cart firstCart = cartService.getCart(session);
        Cart secondCart = cartService.getCart(session);

        assertSame(firstCart, secondCart);
    }

    @Test
    public void getCartForDifferentSessionsTest() {
        HttpSession otherSession = mock(HttpSession.class);
        when(otherSession.getId()).thenReturn(anyString());
        when(otherSession.getAttribute(CART_SESSION_ATTRIBUTE_TEST)).thenReturn(null);

        Cart firstCart = cartService.getCart(session);
        Cart secondCart = cartService.getCart(otherSession);

        assertNotSame(firstCart, secondCart);
    }

    @Test
    public void addNewProductToCartEnoughStockTest() throws OutOfStockException {
        Product product = new Product(2L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        when(productDao.getProduct(product.getId())).thenReturn(product);

        cartService.add(cart, product.getId(), product.getStock());
        CartItem testCartItem = cart.getItems().get(cart.getItems().size() - 1);

        assertEquals(testCartItem.getQuantity(), product.getStock());
        assertEquals(testCartItem.getProduct(), product);
    }

    @Test(expected = OutOfStockException.class)
    public void addNewProductToCartNotEnoughStockTest() throws OutOfStockException {
        Product product = new Product(2L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        when(productDao.getProduct(product.getId())).thenReturn(product);

        cartService.add(cart, product.getId(), product.getStock()+1);
    }

    @Test
    public void addExistedProductToCartEnoughStockTest() throws OutOfStockException {
        CartItem testCartItem = cart.getItems().get(0);
        Product testProduct = testCartItem.getProduct();
        int availableQuantity = testProduct.getStock() - testCartItem.getQuantity();

        when(productDao.getProduct(testProduct.getId())).thenReturn(testProduct);
        cartService.add(cart, testProduct.getId(), availableQuantity);

        assertEquals(testCartItem.getQuantity(), testProduct.getStock());
    }

    @Test(expected = OutOfStockException.class)
    public void addExistedProductToCartNotEnoughStockTest() throws OutOfStockException {
        CartItem testCartItem = cart.getItems().get(0);
        Product testProduct = testCartItem.getProduct();
        int availableQuantity = testProduct.getStock() - testCartItem.getQuantity();

        when(productDao.getProduct(testProduct.getId())).thenReturn(testProduct);
        cartService.add(cart, testProduct.getId(), availableQuantity + 1);
    }

    @Test(expected = WrongItemQuantityException.class)
    public void addProductToCartWrongQuantityTest() throws OutOfStockException {
        cartService.add(cart, 1L, -2);
    }

    @Test
    public void updateCartSuccessTest() throws OutOfStockException {
        CartItem testCartItem = cart.getItems().get(0);
        Product testProduct = testCartItem.getProduct();

        when(productDao.getProduct(testProduct.getId())).thenReturn(testProduct);
        cartService.update(cart, testProduct.getId(), testProduct.getStock());

        assertEquals(testCartItem.getQuantity(), testProduct.getStock());
    }

    @Test(expected = OutOfStockException.class)
    public void updateCartNotEnoughStockTest() throws OutOfStockException {
        CartItem testCartItem = cart.getItems().get(0);
        Product testProduct = testCartItem.getProduct();

        when(productDao.getProduct(testProduct.getId())).thenReturn(testProduct);
        cartService.update(cart, testProduct.getId(), testProduct.getStock() + 1);
    }

    @Test(expected = WrongItemQuantityException.class)
    public void updateCartWrongQuantityTest() throws OutOfStockException {
        cartService.update(cart, 1L, -2);
    }

    @Test
    public void deleteExistedCartItemTest() {
        int oldSize = cart.getItems().size();
        CartItem testCartItem = cart.getItems().get(0);
        Product testProduct = testCartItem.getProduct();
        when(productDao.getProduct(testProduct.getId())).thenReturn(testProduct);

        cartService.delete(cart, testProduct.getId());
        assertFalse(cart.getItems().contains(testCartItem));
        assertNotEquals(oldSize, cart.getItems().size());
    }

    @Test
    public void deleteNotExistedCartItemTest() {
        int oldSize = cart.getItems().size();
        Product product = new Product(2L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        when(productDao.getProduct(product.getId())).thenReturn(null);

        cartService.delete(cart, product.getId());
        assertEquals(oldSize, cart.getItems().size());
    }

    @Test
    public void recalculateCartTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, OutOfStockException {
        int quantityNewProduct = 3;
        Product newProduct = new Product(2L,"sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        when(productDao.getProduct(newProduct.getId())).thenReturn(newProduct);
        cartService.add(cart, newProduct.getId(), quantityNewProduct);

        int quantityExistedProduct = 3;
        Product existedProduct = cart.getItems().get(0).getProduct();
        when(productDao.getProduct(existedProduct.getId())).thenReturn(existedProduct);
        cartService.update(cart, existedProduct.getId(), quantityExistedProduct);

        Method recalculateCart = DefaultCartService.class.getDeclaredMethod("recalculateCart", Cart.class);
        recalculateCart.setAccessible(true);
        recalculateCart.invoke(cartService, cart);

        int totalCost = newProduct.getPrice().intValue() * quantityNewProduct
                + existedProduct.getPrice().intValue() * quantityExistedProduct;

        assertEquals(cart.getTotalQuantity(), quantityNewProduct + quantityExistedProduct);
        assertEquals(cart.getTotalCost(), new BigDecimal(totalCost));
    }
}
