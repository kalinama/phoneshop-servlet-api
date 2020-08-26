package com.es.phoneshop.web.filters;

import com.es.phoneshop.model.services.security.DosProtectionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;
    @Mock
    DosProtectionService dosProtectionService;

    @InjectMocks
    DosFilter filter;

    @Test
    public void doFilterTest() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(any())).thenReturn(true);
        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterErrorTest() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(any())).thenReturn(false);
        filter.doFilter(request, response, filterChain);

        verify(response).sendError(eq(429));
    }
}
