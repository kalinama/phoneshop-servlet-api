package com.es.phoneshop.web.services;

import com.es.phoneshop.model.exceptions.WrongItemQuantityException;
import org.junit.Test;
import java.util.Locale;

import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;

import static org.junit.Assert.assertEquals;

public class DefaultQuantityParamProcessingServiceTest {

    private QuantityParamProcessingService quantityParamProcessingService = DefaultQuantityParamProcessingService.getInstance();
    private Locale locale = Locale.US;

    @Test
    public void getNumberFromQuantityParamTestSuccess() {
        int quantity = quantityParamProcessingService.getNumberFromQuantityParam(locale, "1,000");
        assertEquals(quantity, 1000);
    }

    @Test
    public void getNumberFromQuantityParamNotNumberError() {
        String error = "";
        try {
            quantityParamProcessingService.getNumberFromQuantityParam(locale, "dd");
        } catch (WrongItemQuantityException e) {
            error = e.getMessage();
        }
        assertEquals(error, NOT_NUMBER);
    }

    @Test
    public void getNumberFromQuantityParamFractionalNumberError() {
        String error = "";
        try {
            quantityParamProcessingService.getNumberFromQuantityParam(locale, "1.9");
        } catch (WrongItemQuantityException e) {
            error = e.getMessage();
        }
        assertEquals(error, FRACTIONAL_NUMBER);
    }

    @Test
    public void getNumberFromQuantityParamNegativeNumberError() {
        String error = "";
        try {
            quantityParamProcessingService.getNumberFromQuantityParam(locale, "-5");
        } catch (WrongItemQuantityException e) {
            error = e.getMessage();
        }
        assertEquals(error, NOT_POSITIVE_NUMBER);
    }
}
