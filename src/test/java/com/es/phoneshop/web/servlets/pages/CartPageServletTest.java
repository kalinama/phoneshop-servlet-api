package com.es.phoneshop.web.servlets.pages;

import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.cart.exception.WrongItemQuantityException;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.services.dataprocessing.ParamProcessingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    RequestDispatcher requestDispatcher;
    @Mock
    CartService cartService;
    @Mock
    ParamProcessingService quantityParamProcessingService;

    @InjectMocks
    @Spy
    CartPageServlet servlet;

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void doGetTest() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq(CART), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPostErrorQuantityTest() throws ServletException, IOException, OutOfStockException {
        when(quantityParamProcessingService.getQuantityFromParam(any(), anyString()))
                .thenThrow(new WrongItemQuantityException(NOT_NUMBER));

        when(request.getParameterValues(QUANTITY)).thenReturn(new String[]{"100"});
        when(request.getParameterValues(PRODUCT_ID)).thenReturn(new String[]{"2"});

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        servlet.doPost(request, response);

        verify(cartService, never()).update(any(), anyLong(), anyInt());
        verify(request).setAttribute(eq(WRONG_QUANTITY_ERRORS), captor.capture());
        assertEquals(captor.getValue().get(Long.valueOf("2")), NOT_NUMBER);
        verify(servlet).doGet(request, response);
    }

    @Test
    public void doPostErrorOutOfStockTest() throws OutOfStockException, ServletException, IOException {
        int availableStock = 10;
        int quantity = 100;
        long id = 2L;
        doThrow(new OutOfStockException(availableStock)).when(cartService).update(any(), anyLong(), anyInt());

        when(request.getParameterValues(QUANTITY)).thenReturn(new String[]{String.valueOf(quantity)});
        when(request.getParameterValues(PRODUCT_ID)).thenReturn(new String[]{String.valueOf(id)});
        when(quantityParamProcessingService.getQuantityFromParam(any(), anyString()))
                .thenReturn(quantity);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        servlet.doPost(request, response);

        verify(cartService).update(any(), eq(id), eq(quantity));
        verify(request).setAttribute(eq(WRONG_QUANTITY_ERRORS), captor.capture());
        assertEquals(captor.getValue().get(Long.valueOf("2")), NOT_ENOUGH_STOCK + availableStock);
        verify(servlet).doGet(request, response);
    }

    @Test
    public void doPostSuccessTest() throws ServletException, IOException, OutOfStockException {
        int quantity = 1;
        long id = 2L;
        when(request.getParameterValues(QUANTITY)).thenReturn(new String[]{String.valueOf(quantity)});
        when(request.getParameterValues(PRODUCT_ID)).thenReturn(new String[]{String.valueOf(id)});
        when(quantityParamProcessingService.getQuantityFromParam(any(), anyString()))
                .thenReturn(quantity);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        servlet.doPost(request, response);

        verify(cartService).update(any(), eq(id), eq(quantity));
        verify(request).setAttribute(eq(WRONG_QUANTITY_ERRORS), captor.capture());
        assertTrue(captor.getValue().isEmpty());
        verify(response).sendRedirect(anyString());
    }
}

