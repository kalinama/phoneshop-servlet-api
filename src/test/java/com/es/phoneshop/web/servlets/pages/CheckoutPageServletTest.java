package com.es.phoneshop.web.servlets.pages;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.service.OrderService;
import com.es.phoneshop.model.services.dataprocessing.ParamProcessingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
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
    @Mock
    OrderService orderService;
    @Mock
    ParamProcessingService quantityParamProcessingService;

    @InjectMocks
    CheckoutPageServlet servlet;

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(httpSession);
    }

    @Test
    public void doGetTest() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ORDER), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPostErrorQuantityTest() throws ServletException, IOException {
        when(request.getParameter(FIRST_NAME)).thenReturn("");
        when(quantityParamProcessingService.getErrorOnEmptyParameter(anyString()))
                .thenReturn(EMPTY_VALUE);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        servlet.doPost(request, response);

        verify(request).setAttribute(eq(ORDER_ERRORS), captor.capture());
        assertEquals(captor.getValue().get(FIRST_NAME), EMPTY_VALUE);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPostSuccessTest() throws ServletException, IOException{
        when(orderService.getOrder(any())).thenReturn(new Order());
        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        servlet.doPost(request, response);

        verify(orderService).setCustomerData(any(),any());
        verify(orderService).placeOrder(any());
        verify(cartService).clearCart(any());
        verify(request).setAttribute(eq(ORDER_ERRORS), captor.capture());
        assertTrue(captor.getValue().isEmpty());
        verify(response).sendRedirect(anyString());
    }
}
