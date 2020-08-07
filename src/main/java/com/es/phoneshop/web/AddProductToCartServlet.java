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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;

public class AddProductToCartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageUrlParameter = request.getParameter(PAGE_URL);
        Map<String, String> parametersMap = getParametersMapForRequest(request);
        String url = pageUrlParameter + "?" + getParametersInLine(parametersMap);
        response.sendRedirect(url);
    }

    private Map<String, String> getParametersMapForRequest (HttpServletRequest request) {
        String pageUrlParameter = request.getParameter(PAGE_URL);
        String quantityParameter = request.getParameter(QUANTITY);
        String productId = request.getPathInfo().substring(1);
        String error = getErrorTypeOfQuantityParameter(request, productId, quantityParameter);

        Map<String, String> map = new HashMap<>();
        initParametersMap(request, map);

        if (error!=null) {
            map.put(WRONG_QUANTITY_ERROR, error);
            map.put(QUANTITY, quantityParameter);
        }
        else
            map.put(MESSAGE, "Added to cart successfully");

        if (!pageUrlParameter.contains("products/"))
            map.put(PRODUCT_ID, productId);

        return map;
    }

    private void initParametersMap(HttpServletRequest request, Map<String, String> map) {
        addParameterToMapIfExist(request, map, SORT);
        addParameterToMapIfExist(request, map, ORDER);
        addParameterToMapIfExist(request, map, QUERY);
    }

    private void addParameterToMapIfExist(HttpServletRequest request, Map<String, String> parametersMap, String parameterName) {
        String parameter = request.getParameter(parameterName);
        if (parameter!=null)
            parametersMap.put(parameterName, parameter);
    }

    private String getParametersInLine (Map<String, String> parametersMap){
        return parametersMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(PAGE_URL))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private String getErrorTypeOfQuantityParameter(HttpServletRequest request, String idParameter, String quantityParameter) {
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
            cartService.add(cart, id, quantity);
        } catch (OutOfStockException e) {
            return "Not enough stock. Available: " + e.getAvailableStock();
        }
        return null;
    }

}
