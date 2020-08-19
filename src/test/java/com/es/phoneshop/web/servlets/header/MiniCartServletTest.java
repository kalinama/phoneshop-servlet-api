package com.es.phoneshop.web.servlets.header;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    RequestDispatcher requestDispatcher;
    @Mock
    HttpSession httpSession;
    @Mock
    CartService cartService;

    @InjectMocks
    MiniCartServlet miniCartServlet;

    @Before
    public void setup() {
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(httpSession);
    }

    @Test
    public void doGetTest() throws ServletException, IOException {
        when(cartService.getCart(any(HttpSession.class))).thenReturn(new Cart());
        miniCartServlet.doGet(request, response);

        verify(request).setAttribute(eq(CART), any(Cart.class));
        verify(requestDispatcher).include(request, response);
    }

    @Test
    public void doPostTest() throws ServletException, IOException {
        when(cartService.getCart(any(HttpSession.class))).thenReturn(new Cart());
        miniCartServlet.doPost(request, response);

        verify(request).setAttribute(eq(CART), any(Cart.class));
        verify(requestDispatcher).include(request, response);
    }
}
