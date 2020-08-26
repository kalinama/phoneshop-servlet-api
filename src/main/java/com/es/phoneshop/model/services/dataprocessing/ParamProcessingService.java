package com.es.phoneshop.model.services.dataprocessing;

import com.es.phoneshop.model.cart.exception.WrongItemQuantityException;

import java.math.BigDecimal;
import java.util.Locale;

public interface ParamProcessingService {

    int getQuantityFromParam(Locale locale, String quantityParameter) throws WrongItemQuantityException;
    BigDecimal getPriceFromParam(Locale locale, String priceParam) throws WrongItemQuantityException;
    String getErrorOnEmptyParameter(String param);
    String getErrorOnPhoneParameter(String phoneParam);
    String getErrorOnDateParameter(String dateParam);
    String getErrorOnPaymentMethodParameter(String paymentMethodParam);
    String getErrorOnQuantityParameter(Locale locale, String quantityParam);
    String getErrorOnPrice(Locale locale, String priceParam);

}
