package com.es.phoneshop.web.services;

import com.es.phoneshop.model.cart.exception.WrongItemQuantityException;

import java.util.Locale;

public interface ParamProcessingService {

    int getQuantityFromParam(Locale locale, String quantityParameter) throws WrongItemQuantityException;
    String getErrorOnEmptyParameter(String param);
    String getErrorOnPhoneParameter(String phoneParam);
    String getErrorOnDateParameter(String dateParam);
    String getErrorOnPaymentMethodParameter(String paymentMethodParam);

}
