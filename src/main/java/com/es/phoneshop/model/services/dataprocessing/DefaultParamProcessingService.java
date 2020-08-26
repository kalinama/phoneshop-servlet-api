package com.es.phoneshop.model.services.dataprocessing;

import com.es.phoneshop.model.order.enums.PaymentMethod;
import com.es.phoneshop.model.cart.exception.WrongItemQuantityException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;

public class DefaultParamProcessingService implements ParamProcessingService {

    private DefaultParamProcessingService() {}

    private static class DefaultRequestProcessingServiceHolder {
        static final DefaultParamProcessingService HOLDER_INSTANCE = new DefaultParamProcessingService();
    }

    public static DefaultParamProcessingService getInstance() {
        return DefaultRequestProcessingServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public int getQuantityFromParam(Locale locale, String quantityParam) throws WrongItemQuantityException {
        int quantity;
        double quantityFractional;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            quantityFractional = numberFormat.parse(quantityParam).doubleValue();
            quantity = (int) quantityFractional;
        } catch (ParseException e) {
           throw new WrongItemQuantityException(NOT_NUMBER);
        }

        if (quantityFractional != quantity)
            throw new WrongItemQuantityException(FRACTIONAL_NUMBER);

        if (quantity <= 0)
            throw new WrongItemQuantityException(NOT_POSITIVE_NUMBER);

        return quantity;
    }

    @Override
    public BigDecimal getPriceFromParam(Locale locale, String priceParam) throws WrongItemQuantityException {
        if (priceParam == null || priceParam.isEmpty())
            return null;
        double price;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            price = numberFormat.parse(priceParam).doubleValue();
        } catch (ParseException e) {
            throw new WrongItemQuantityException(NOT_NUMBER);
        }

        if (price <= 0)
            throw new WrongItemQuantityException(NOT_POSITIVE_NUMBER);

        return new BigDecimal(price);
    }

    @Override
    public String getErrorOnEmptyParameter(String param) {
        if (param == null || param.isEmpty())
            return EMPTY_VALUE;

        return null;
    }

    @Override
    public String getErrorOnPhoneParameter(String phoneParam) {
        if (getErrorOnEmptyParameter(phoneParam)!= null)
            return getErrorOnEmptyParameter(phoneParam);

        if (!phoneParam.matches("\\+375[\\-]?[0-9]{2}[\\-]?[0-9]{3}[\\-]?[0-9]{2}[\\-]?[0-9]{2}"))
            return NOT_MATCH_FORMAT;

        return null;
    }

    @Override
    public String getErrorOnDateParameter(String dateParam) {
        if (getErrorOnEmptyParameter(dateParam)!= null)
            return getErrorOnEmptyParameter(dateParam);

        try {
            LocalDate.parse(dateParam);
        } catch (DateTimeParseException e) {
            return NOT_CORRECT_DATE_VALUE;
        }
        return null;
    }

    @Override
    public String getErrorOnPaymentMethodParameter(String paymentMethodParam) {
        if (getErrorOnEmptyParameter(paymentMethodParam)!= null)
            return getErrorOnEmptyParameter(paymentMethodParam);

        try {
            PaymentMethod.valueOf(paymentMethodParam);
        } catch (IllegalArgumentException e) {
            return NOT_CORRECT_VALUE;
        }
        return null;
    }

    @Override
    public String getErrorOnQuantityParameter(Locale locale, String quantityParam) {
        if (getErrorOnEmptyParameter(quantityParam)!= null)
            return null;
        try {
            getQuantityFromParam(locale, quantityParam);
        } catch (WrongItemQuantityException e) {
            return e.getMessage();
        }
        return null;
    }

    @Override
    public String getErrorOnPrice(Locale locale, String priceParam) {
        if (getErrorOnEmptyParameter(priceParam)!= null)
            return null;
        try {
            getPriceFromParam(locale, priceParam);
        } catch (WrongItemQuantityException e) {
            return e.getMessage();
        }
        return null;
    }
}
