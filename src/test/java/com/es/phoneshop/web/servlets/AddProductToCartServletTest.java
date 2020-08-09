package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.exceptions.WrongItemQuantityException;
import com.es.phoneshop.web.services.QuantityParamProcessingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class AddProductToCartServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    QuantityParamProcessingService quantityParamProcessingService;
    @Mock
    CartService cartService;

    @InjectMocks
    AddProductToCartServlet servlet;

    private Long id = 2L;

    public AddProductToCartServletTest() throws NoSuchFieldException, IllegalAccessException {
        servlet = new AddProductToCartServlet();
        Field field = AddProductToCartServlet.class.getDeclaredField("parameterNames");
        field.setAccessible(true);
        field.set(servlet, Arrays.asList(SORT, ORDER, QUERY));
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setup(){
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getPathInfo()).thenReturn("/" + id);
    }

    @Test
    public void doPostSuccessWithoutAdditionalParametersForPLPTest() throws ServletException, IOException, OutOfStockException {
        String quantity = "1";
        String pageUrl = "/phoneshop-servlet-api/products";

        when(quantityParamProcessingService.getNumberFromQuantityParam(any(), anyString()))
                .thenReturn(Integer.valueOf(quantity));
        when(request.getParameter(PAGE_URL)).thenReturn(pageUrl);
        when(request.getParameter(QUANTITY)).thenReturn(quantity);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        servlet.doPost(request, response);

        verify(cartService).add(any(), eq(id), eq(Integer.valueOf(quantity)));
        verify(response).sendRedirect(captor.capture());
        assertTrue(captor.getValue().startsWith(pageUrl));
        assertTrue(captor.getValue().contains(PRODUCT_ID + "=" + id));
        assertTrue(captor.getValue().contains(MESSAGE + "=" + ADD_TO_CART_SUCCESSFULLY));
    }

    @Test
    public void doPostQuantityErrorWithAdditionalParametersForPLPTest() throws ServletException, IOException, OutOfStockException {
        String quantity = "d";
        String pageUrl = "/phoneshop-servlet-api/products";

        when(quantityParamProcessingService.getNumberFromQuantityParam(any(Locale.class), anyString()))
                .thenThrow(new WrongItemQuantityException(NOT_NUMBER));
        when(request.getParameter(PAGE_URL)).thenReturn(pageUrl);
        when(request.getParameter(QUANTITY)).thenReturn(quantity);
        when(request.getParameter(SORT)).thenReturn("price");
        when(request.getParameter(ORDER)).thenReturn("desc");
        when(request.getParameter(QUERY)).thenReturn("samsung");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        servlet.doPost(request, response);

        verify(cartService, never()).add(any(), anyLong(), anyInt());
        verify(response).sendRedirect(captor.capture());
        assertTrue(captor.getValue().startsWith(pageUrl));
        assertTrue(captor.getValue().contains(ORDER + "=desc"));
        assertTrue(captor.getValue().contains(SORT + "=price"));
        assertTrue(captor.getValue().contains(QUERY + "=samsung"));
        assertTrue(captor.getValue().contains(PRODUCT_ID + "=" + id));
        assertTrue(captor.getValue().contains(QUANTITY + "=" + quantity));
        assertTrue(captor.getValue().contains(WRONG_QUANTITY_ERROR + "=" + NOT_NUMBER));
    }

    @Test
    public void doPostSuccessWithoutAdditionalParametersForPDPTest() throws ServletException, IOException, OutOfStockException {
        String quantity = "1";
        String pageUrl = "/phoneshop-servlet-api/products/" + id;

        when(quantityParamProcessingService.getNumberFromQuantityParam(any(), anyString()))
                .thenReturn(Integer.valueOf(quantity));
        when(request.getParameter(PAGE_URL)).thenReturn(pageUrl);
        when(request.getParameter(QUANTITY)).thenReturn(quantity);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        servlet.doPost(request, response);

        verify(cartService).add(any(), eq(id), eq(Integer.valueOf(quantity)));
        verify(response).sendRedirect(captor.capture());
        assertTrue(captor.getValue().startsWith(pageUrl));
        assertFalse(captor.getValue().contains(PRODUCT_ID + "=" + id));
        assertTrue(captor.getValue().contains(MESSAGE + "=" + ADD_TO_CART_SUCCESSFULLY));
    }

    @Test
    public void doPostErrorOutStockWithoutAdditionalParametersForPDPTest() throws ServletException, IOException, OutOfStockException {
        String quantity = "100";
        String pageUrl = "/phoneshop-servlet-api/products/" + id;
        int availableStock = 10;

        when(quantityParamProcessingService.getNumberFromQuantityParam(any(), anyString()))
                .thenReturn(Integer.valueOf(quantity));
        doThrow(new OutOfStockException(availableStock)).when(cartService).add(any(), anyLong(), anyInt());
        when(request.getParameter(PAGE_URL)).thenReturn(pageUrl);
        when(request.getParameter(QUANTITY)).thenReturn(quantity);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        servlet.doPost(request, response);

        verify(cartService).add(any(), eq(id), eq(Integer.valueOf(quantity)));
        verify(response).sendRedirect(captor.capture());
        assertTrue(captor.getValue().startsWith(pageUrl));
        assertFalse(captor.getValue().contains(PRODUCT_ID + "=" + id));
        assertTrue(captor.getValue().contains(QUANTITY + "=" + quantity));
        assertTrue(captor.getValue().contains(WRONG_QUANTITY_ERROR + "=" + NOT_ENOUGH_STOCK));
    }
}
