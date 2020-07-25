package com.es.phoneshop.web.productDetails;

import com.es.phoneshop.model.product.dao.ProductDao;
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
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceHistoryPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductPriceHistoryPageServlet servlet;

    @Before
    public void setup() {

       // when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
//        when(request.getPathInfo()).thenReturn("/1");
//        servlet.doGet(request, response);
//
//        verify(requestDispatcher).forward(request, response);
//        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/productPriceHistory.jsp"));
//        verify(request).setAttribute(eq("product"), any());
    }

}
