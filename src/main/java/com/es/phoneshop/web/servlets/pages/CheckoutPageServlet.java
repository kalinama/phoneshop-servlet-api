package com.es.phoneshop.web.servlets.pages;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.service.DefaultOrderService;
import com.es.phoneshop.model.order.service.OrderService;
import com.es.phoneshop.model.services.dataprocessing.DefaultParamProcessingService;
import com.es.phoneshop.model.services.dataprocessing.ParamProcessingService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;

public class CheckoutPageServlet extends HttpServlet {

    private CartService cartService;
    private OrderService orderService;
    private ParamProcessingService paramProcessingService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
        paramProcessingService = DefaultParamProcessingService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        request.setAttribute(ORDER, orderService.getOrder(cart));
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order =  orderService.getOrder(cart);
        request.setAttribute(ORDER, orderService.getOrder(cart));

        Map<String,String> errors = getParametersErrorsFromRequest(request);
        request.setAttribute(ORDER_ERRORS, errors);

        if(errors.isEmpty()) {
            setOrderCustomerFields(order, request);
            orderService.placeOrder(order);
            cartService.clearCart(request.getSession());
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        }
        else
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    private Map<String,String> getParametersErrorsFromRequest(HttpServletRequest request){
        Map<String,String> errors = new HashMap<>();
        checkParameter(FIRST_NAME, request.getParameter(FIRST_NAME), errors, paramProcessingService::getErrorOnEmptyParameter);
        checkParameter(LAST_NAME, request.getParameter(LAST_NAME), errors, paramProcessingService::getErrorOnEmptyParameter);
        checkParameter(PHONE, request.getParameter(PHONE), errors, paramProcessingService::getErrorOnPhoneParameter);
        checkParameter(DELIVERY_DATE, request.getParameter(DELIVERY_DATE), errors, paramProcessingService::getErrorOnDateParameter);
        checkParameter(DELIVERY_ADDRESS, request.getParameter(DELIVERY_ADDRESS), errors, paramProcessingService::getErrorOnEmptyParameter);
        checkParameter(PAYMENT_METHOD, request.getParameter(PAYMENT_METHOD), errors, paramProcessingService::getErrorOnPaymentMethodParameter);

        return errors;
    }

    private void checkParameter(String paramName, String paramValue, Map<String,String> errors,
                                              UnaryOperator<String> unaryOperator){
        String error = unaryOperator.apply(paramValue);
        if(error != null)
            errors.put(paramName, error);
    }

    private void setOrderCustomerFields(Order order, HttpServletRequest request){
        Map<String, String> parametersMap = new HashMap<>();
        List<String> paramNames = Arrays.asList(FIRST_NAME, LAST_NAME, PHONE, DELIVERY_DATE, DELIVERY_ADDRESS, PAYMENT_METHOD);
        for (String paramName: paramNames)
            parametersMap.put(paramName, request.getParameter(paramName));
        orderService.setCustomerData(order, parametersMap);
    }

}
