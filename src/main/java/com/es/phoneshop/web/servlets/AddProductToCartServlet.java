package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.web.services.DefaultQuantityParamProcessingService;
import com.es.phoneshop.web.services.QuantityParamProcessingService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;

public class AddProductToCartServlet extends HttpServlet {

    private CartService cartService;
    private QuantityParamProcessingService quantityParamService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        quantityParamService = DefaultQuantityParamProcessingService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageUrlParameter = request.getParameter(PAGE_URL);
        Map<String, String> parametersMap = getParametersMapFromRequest(request);
        String url = pageUrlParameter + "?" + getParametersInLine(parametersMap);
        response.sendRedirect(url);
    }

    private Map<String, String> getParametersMapFromRequest(HttpServletRequest request) {
        String pageUrlParameter = request.getParameter(PAGE_URL);
        String quantityParameter = request.getParameter(QUANTITY);
        String productId = request.getPathInfo().substring(1);
        String error = quantityParamService.getErrorTypeOfQuantityForAdd(request, productId, quantityParameter);

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
        addParameterToMapIfExist(request, map, SORT); //specify parameters that must be in url after redirect
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
}
