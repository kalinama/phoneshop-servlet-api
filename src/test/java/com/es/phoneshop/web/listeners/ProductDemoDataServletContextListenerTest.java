package com.es.phoneshop.web.listeners;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.web.listeners.ProductDemoDataServletContextListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDemoDataServletContextListenerTest {

    @Mock
    ServletContextEvent servletContextEvent;
    @Mock
    ServletContext servletContext;
    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductDemoDataServletContextListener productDemoDataServletContextListener;

    @Test
    public void testAddDemoDataTest() {
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(anyString())).thenReturn("true");
        productDemoDataServletContextListener.contextInitialized(servletContextEvent);

        verify(productDao, atLeast(1)).save(any(Product.class));
    }

    @Test
    public void testNotAddDemoDataTest() {
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(anyString())).thenReturn("false");
        productDemoDataServletContextListener.contextInitialized(servletContextEvent);

        verify(productDao, times(0)).save(any(Product.class));
    }
}
