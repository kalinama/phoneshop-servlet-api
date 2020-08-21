package com.es.phoneshop.model.services.dataprocessing;

import com.es.phoneshop.model.order.enums.PaymentMethod;
import com.es.phoneshop.model.cart.exception.WrongItemQuantityException;

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
    public String getErrorOnEmptyParameter(String param) {
        if (param == null || param.isEmpty())
            return "Value is empty";

        return null;
    }

    @Override
    public String getErrorOnPhoneParameter(String phoneParam) {
        if (getErrorOnEmptyParameter(phoneParam)!= null)
            return getErrorOnEmptyParameter(phoneParam);

        if (!phoneParam.matches("\\+375[\\-]?[0-9]{2}[\\-]?[0-9]{3}[\\-]?[0-9]{2}[\\-]?[0-9]{2}"))
            return "The entered value does not match the format";

        return null;
    }

    @Override
    public String getErrorOnDateParameter(String dateParam) {
        if (getErrorOnEmptyParameter(dateParam)!= null)
            return getErrorOnEmptyParameter(dateParam);

        try {
            LocalDate.parse(dateParam);
        } catch (DateTimeParseException e) {
            return "The entered date value not correct";
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
            return "The entered value not correct";
        }
        return null;
    }
}
