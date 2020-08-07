package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.exceptions.OutOfStockException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;

public class CartPageServlet extends HttpServlet {

    private CartService cartService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
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
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        else
            doGet(request, response);
    }

    private Map<Long,String> getErrorsFromRequest(HttpServletRequest request)
    {
        List<String> quantityParameters = Arrays.asList(request.getParameterValues(QUANTITY));
        List<String> idParameters = Arrays.asList(request.getParameterValues(PRODUCT_ID));

        return idParameters.stream()
                .map(idParam -> new AbstractMap.SimpleImmutableEntry<>(Long.valueOf(idParam),
                        getErrorTypeOfQuantityParameter(request, idParam,
                                quantityParameters.get(idParameters.indexOf(idParam)))))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getErrorTypeOfQuantityParameter(HttpServletRequest request, String  idParameter, String quantityParameter) {
        int quantity;
        double quantityFractional;
        Long id = Long.valueOf(idParameter);
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            quantityFractional = numberFormat.parse(quantityParameter).doubleValue();
            quantity = (int) quantityFractional;
        } catch (ParseException e) {
            return "Not a number";
        }

        if (quantityFractional != quantity)
           return "Can't enter fractional number";

        if (quantity <= 0)
           return "Can't add 0 or negative number of items";

        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.update(cart, id, quantity);
        } catch (OutOfStockException e) {
            return "Not enough stock. Available: " + e.getAvailableStock();
        }
        return null;
    }
}
