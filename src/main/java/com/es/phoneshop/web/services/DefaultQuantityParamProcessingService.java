package com.es.phoneshop.web.services;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.exceptions.WrongItemQuantityException;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;

public class DefaultQuantityParamProcessingService implements QuantityParamProcessingService {

    private CartService cartService;

    private DefaultQuantityParamProcessingService() {
        cartService = DefaultCartService.getInstance();
    }

    private static class DefaultRequestProcessingServiceHolder {
        static final DefaultQuantityParamProcessingService HOLDER_INSTANCE = new DefaultQuantityParamProcessingService();
    }

    public static DefaultQuantityParamProcessingService getInstance() {
        return DefaultRequestProcessingServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public int getQuantityFromRequest(HttpServletRequest request, String quantityParam) throws WrongItemQuantityException {
        int quantity;
        double quantityFractional;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            quantityFractional = numberFormat.parse(quantityParam).doubleValue();
            quantity = (int) quantityFractional;
        } catch (ParseException e) {
           throw new WrongItemQuantityException("Not a number");
        }

        if (quantityFractional != quantity)
            throw new WrongItemQuantityException("Can't enter fractional number");

        if (quantity <= 0)
            throw new WrongItemQuantityException("Can't add 0 or negative number of items");

        return quantity;
    }

    @Override
    public String getErrorTypeOfQuantityForAdd(HttpServletRequest request, String idParam, String quantityParam){
        int quantity;
        try {
            quantity = getQuantityFromRequest(request, quantityParam);
        } catch (WrongItemQuantityException e) {
            return e.getMessage();
        }

        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart, Long.valueOf(idParam), quantity);
        } catch (OutOfStockException e) {
            return "Not enough stock. Available: " + e.getAvailableStock();
        }
        return null;
    }

    @Override
    public String getErrorTypeOfQuantityForUpdate(HttpServletRequest request, String idParam, String quantityParam){
        int quantity;
        try {
            quantity = getQuantityFromRequest(request, quantityParam);
        } catch (WrongItemQuantityException e) {
            return e.getMessage();
        }

        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.update(cart, Long.valueOf(idParam), quantity);
        } catch (OutOfStockException e) {
            return "Not enough stock. Available: " + e.getAvailableStock();
        }
        return null;
    }

}
