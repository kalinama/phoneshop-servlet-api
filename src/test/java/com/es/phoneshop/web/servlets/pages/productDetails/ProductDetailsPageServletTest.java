package com.es.phoneshop.web.servlets.pages.productDetails;

import com.es.phoneshop.model.cart.service.CartService;
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
        verify(request).setAttribute(eq("viewedProducts"), anyList());
    }

}

