package com.es.phoneshop.web.services;

import com.es.phoneshop.model.exceptions.WrongItemQuantityException;

import javax.servlet.http.HttpServletRequest;

public interface QuantityParamProcessingService {

    int getQuantityFromRequest(HttpServletRequest request, String quantityParameter) throws WrongItemQuantityException;
    String getErrorTypeOfQuantityForAdd(HttpServletRequest request, String idParam, String quantityParam);
    String getErrorTypeOfQuantityForUpdate(HttpServletRequest request, String idParam, String quantityParam);
}
