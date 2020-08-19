package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemDeleteServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    CartService cartService;

    @InjectMocks
    CartItemDeleteServlet servlet;

    @Test
    public void doPostTest() throws ServletException, IOException {
        long id = 1L;
        when(request.getPathInfo()).thenReturn("/" + id);
        servlet.doPost(request, response);

        verify(cartService).delete(any(), eq(id));
        verify(response).sendRedirect(anyString());
    }
}
