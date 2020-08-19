package com.es.phoneshop.web.servlets.pages;

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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
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
    private ViewedProductsService viewedProductsService;

    @InjectMocks
    private ProductListPageServlet servlet;


    @Before
    public void setup() {

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(httpSession);
    }

    @Test
    public void testDoGetTest() throws ServletException, IOException {
        when(viewedProductsService.getViewedProductsUnit(httpSession)).thenReturn(new ViewedProductsUnit());

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/productList.jsp"));
        verify(request).setAttribute(eq("products"), any());
        verify(request).setAttribute(eq("viewedProducts"), anyList());
    }
}
