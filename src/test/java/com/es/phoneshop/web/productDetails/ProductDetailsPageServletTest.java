package com.es.phoneshop.web.productDetails;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ViewedProductsUnit;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.service.ViewedProductsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static junit.framework.TestCase.assertNull;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession httpSession;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;
    @Mock
    private ViewedProductsService viewedProductsService;

    @InjectMocks
    private ProductDetailsPageServlet servlet;

    @Before
    public void setup() {
      when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
      when(request.getSession()).thenReturn(httpSession);
      when(request.getLocale()).thenReturn(Locale.US);
      when(request.getContextPath()).thenReturn("/phoneshop-servlet-api");
    }

    @Test
    public void testDoGetTest() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(viewedProductsService.getViewedProductsUnit(httpSession)).thenReturn(new ViewedProductsUnit());
        doNothing().when(viewedProductsService).addProductToViewed(any(ViewedProductsUnit.class), anyLong());

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/productDetails.jsp"));
        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("cart"), any());
        verify(request).setAttribute(eq("viewedProducts"), anyList());
    }

    @Test
    public void testDoPostCorrectTest() throws ServletException, IOException {
        String id = "1";
        when(request.getPathInfo()).thenReturn("/" + id);
        when(request.getParameter("quantity")).thenReturn("1");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/phoneshop-servlet-api/products/" + id + "?message=Added to cart successfully");
        assertNull(request.getParameter("addToCartError"));
    }

    @Test
    public void testDoPostOutOfStockTest() throws OutOfStockException, ServletException, IOException { when(request.getPathInfo()).thenReturn("/1");
        when(cartService.getCart(httpSession)).thenReturn(new Cart());
        doThrow(OutOfStockException.class).when(cartService).add(any(Cart.class), anyLong(), anyInt());
        checkIncorrectlyEnteredQuantity("100");
    }

    @Test
    public void testDoPostNotNumberErrorTest() throws ServletException, IOException {
        checkIncorrectlyEnteredQuantity("ddd");
    }

    @Test
    public void testDoPostZeroErrorTest() throws ServletException, IOException {
        checkIncorrectlyEnteredQuantity("0");
    }

    @Test
    public void testDoPostFractionErrorTest() throws ServletException, IOException { // US Locale
        checkIncorrectlyEnteredQuantity("1.7");
    }

    private void checkIncorrectlyEnteredQuantity(String enteredValue) throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("quantity")).thenReturn(enteredValue);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("addToCartError"), anyString());
    }

}

