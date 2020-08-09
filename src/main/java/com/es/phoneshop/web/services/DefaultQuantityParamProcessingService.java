package com.es.phoneshop.web.services;

import com.es.phoneshop.model.exceptions.WrongItemQuantityException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;

public class DefaultQuantityParamProcessingService implements QuantityParamProcessingService {

    private DefaultQuantityParamProcessingService() {}

    private static class DefaultRequestProcessingServiceHolder {
        static final DefaultQuantityParamProcessingService HOLDER_INSTANCE = new DefaultQuantityParamProcessingService();
    }

    public static DefaultQuantityParamProcessingService getInstance() {
        return DefaultRequestProcessingServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public int getNumberFromQuantityParam(Locale locale, String quantityParam) throws WrongItemQuantityException {
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
}
