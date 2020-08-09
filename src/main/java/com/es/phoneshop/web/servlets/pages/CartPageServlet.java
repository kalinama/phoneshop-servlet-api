package com.es.phoneshop.web.servlets.pages;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.exceptions.WrongItemQuantityException;
import com.es.phoneshop.web.services.DefaultQuantityParamProcessingService;
import com.es.phoneshop.web.services.QuantityParamProcessingService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;


public class CartPageServlet extends HttpServlet {

    private CartService cartService;
    private QuantityParamProcessingService quantityParamService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        quantityParamService = DefaultQuantityParamProcessingService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART, cartService.getCart(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long,String> errors = getErrorsFromRequest(request);
        request.setAttribute(WRONG_QUANTITY_ERRORS, errors);

        if (errors.isEmpty())
            response.sendRedirect(request.getContextPath() + "/cart?" + MESSAGE + "=" + UPDATE_CART_SUCCESSFULLY );
        else
            doGet(request, response);
    }

    private Map<Long,String> getErrorsFromRequest(HttpServletRequest request)
    {
        List<String> quantityParameters = Arrays.asList(request.getParameterValues(QUANTITY));
        List<String> idParameters = Arrays.asList(request.getParameterValues(PRODUCT_ID));

        return idParameters.stream()
                .map(idParam -> new AbstractMap.SimpleImmutableEntry<>(Long.valueOf(idParam),
                        getErrorMessageOnCartUpdating(request, idParam,
                                quantityParameters.get(idParameters.indexOf(idParam)))))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getErrorMessageOnCartUpdating(HttpServletRequest request, String idParam, String quantityParam){
        int quantity;
        try {
            quantity = quantityParamService.getNumberFromQuantityParam(request.getLocale(), quantityParam);
        } catch (WrongItemQuantityException e) {
            return e.getMessage();
        }

        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.update(cart, Long.valueOf(idParam), quantity);
        } catch (OutOfStockException e) {
            return NOT_ENOUGH_STOCK + e.getAvailableStock();
        }
        return null;
    }
}
