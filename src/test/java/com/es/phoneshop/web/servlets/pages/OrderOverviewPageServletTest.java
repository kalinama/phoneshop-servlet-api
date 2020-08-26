package com.es.phoneshop.web.servlets.pages;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.dao.OrderDao;
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

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    RequestDispatcher requestDispatcher;
    @Mock
    OrderDao orderDao;

    @InjectMocks
    OrderOverviewPageServlet servlet;

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/1");
    }

    @Test
    public void doGetTest() throws ServletException, IOException {
        when(orderDao.getBySecureId(anyString())).thenReturn(new Order());
        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ORDER), any(Order.class));
        verify(requestDispatcher).forward(request, response);
    }
}
