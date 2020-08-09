package com.es.phoneshop.web.services;

import com.es.phoneshop.model.exceptions.WrongItemQuantityException;
import java.util.Locale;

public interface QuantityParamProcessingService {

    int getNumberFromQuantityParam(Locale locale, String quantityParameter) throws WrongItemQuantityException;
}
