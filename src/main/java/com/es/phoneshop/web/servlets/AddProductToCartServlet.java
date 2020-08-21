package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.cart.exception.WrongItemQuantityException;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.web.enums.ApplicationPages;
import com.es.phoneshop.web.services.DefaultParamProcessingService;
import com.es.phoneshop.web.services.ParamProcessingService;

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

public class AddProductToCartServlet extends HttpServlet {

    private CartService cartService;
    private ParamProcessingService paramProcessingService;
    private List<String> parameterNames;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        paramProcessingService = DefaultParamProcessingService.getInstance();
        parameterNames = Arrays.asList(SORT, SORTING_ORDER, QUERY); //specify parameters that must be in url after redirect
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageUrlParameter = getUrlFromRequest(request);
        Map<String, String> parametersMap = getParametersMapOnAddingToCartFromRequest(request);
        String url = pageUrlParameter + "?" + getParametersInLine(parametersMap);
        response.sendRedirect(url);
    }

    private Map<String, String> getParametersMapOnAddingToCartFromRequest(HttpServletRequest request) {
        String pageUrlParameter = getUrlFromRequest(request);
        String quantityParameter = request.getParameter(QUANTITY);
        String productId = request.getPathInfo().substring(1);

        String error = getErrorMessageOnAddingToCart(request, productId, quantityParameter);
        Map<String, String> map = getInitializedParametersMap(request, parameterNames);

        if (error!=null) {
            map.put(WRONG_QUANTITY_ERROR, error);
            map.put(QUANTITY, quantityParameter);
        }
        else
            map.put(MESSAGE, ADD_TO_CART_SUCCESSFULLY);

        if (pageUrlParameter.endsWith("/products"))
            map.put(PRODUCT_ID, productId);

        return map;
    }

    private Map<String, String> getInitializedParametersMap(HttpServletRequest request, List<String> parameterNames) {
        Map<String, String> map = new HashMap<>();
        for (String parameterName: parameterNames){
            String parameter = request.getParameter(parameterName);
            if (parameter!=null)
                map.put(parameterName, parameter);
        }
        return map;
    }

    private String getParametersInLine (Map<String, String> parametersMap){
        return parametersMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private String getErrorMessageOnAddingToCart(HttpServletRequest request, String idParam, String quantityParam){
        int quantity;
        try {
            quantity = paramProcessingService.getQuantityFromParam(request.getLocale(), quantityParam);
        } catch (WrongItemQuantityException e) {
            return e.getMessage();
        }

        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart, Long.valueOf(idParam), quantity);
        } catch (OutOfStockException e) {
            return NOT_ENOUGH_STOCK + e.getAvailableStock();
        }
        return null;
    }

    private String getUrlFromRequest(HttpServletRequest request){
        String pageCode = request.getParameter(PAGE_CODE);
        String pageUrl = ApplicationPages.valueOf(pageCode).getUrl();

        if(pageCode.equals("PDP"))
            pageUrl += request.getPathInfo().substring(1);

        return pageUrl;
    }

}
