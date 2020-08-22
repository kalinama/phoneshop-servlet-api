package com.es.phoneshop.model.services.dataprocessing;

import com.es.phoneshop.model.cart.exception.WrongItemQuantityException;
import org.junit.Test;
import java.util.Locale;

import static com.es.phoneshop.web.constants.ErrorAndSuccessMessageConstants.*;

import static org.junit.Assert.assertEquals;

public class DefaultParamProcessingServiceTest {

    private ParamProcessingService paramProcessingService = DefaultParamProcessingService.getInstance();
    private Locale locale = Locale.US;

    @Test
    public void getNumberFromQuantityParamTestSuccess() {
        int quantity = paramProcessingService.getQuantityFromParam(locale, "1,000");
        assertEquals(quantity, 1000);
    }

    @Test
    public void getNumberFromQuantityParamNotNumberError() {
        String error = "";
        try {
            paramProcessingService.getQuantityFromParam(locale, "dd");
        } catch (WrongItemQuantityException e) {
            error = e.getMessage();
        }
        assertEquals(error, NOT_NUMBER);
    }

    @Test
    public void getNumberFromQuantityParamFractionalNumberError() {
        String error = "";
        try {
            paramProcessingService.getQuantityFromParam(locale, "1.9");
        } catch (WrongItemQuantityException e) {
            error = e.getMessage();
        }
        assertEquals(error, FRACTIONAL_NUMBER);
    }

    @Test
    public void getNumberFromQuantityParamNegativeNumberError() {
        String error = "";
        try {
            paramProcessingService.getQuantityFromParam(locale, "-5");
        } catch (WrongItemQuantityException e) {
            error = e.getMessage();
        }
        assertEquals(error, NOT_POSITIVE_NUMBER);
    }

    @Test
    public void getErrorOnEmptyParameterTest(){
        String error = paramProcessingService.getErrorOnEmptyParameter("");
        assertEquals(error, EMPTY_VALUE);
    }

    @Test
    public void getErrorOnPhoneParameterTest(){
        String error = paramProcessingService.getErrorOnPhoneParameter("+3754758");
        assertEquals(error, NOT_MATCH_FORMAT);
    }

    @Test
    public void getErrorOnDateParameterTest(){
        String error = paramProcessingService.getErrorOnDateParameter("2000-12-67");
        assertEquals(error, NOT_CORRECT_DATE_VALUE);
    }

    @Test
    public void getErrorOnPaymentMethodParameterTest(){
        String error = paramProcessingService.getErrorOnPaymentMethodParameter("hh");
        assertEquals(error, NOT_CORRECT_VALUE);
    }
}
