package com.es.phoneshop.web.servlets.pages;

import com.es.phoneshop.model.product.AdvancedProductDescription;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.services.dataprocessing.DefaultParamProcessingService;
import com.es.phoneshop.model.services.dataprocessing.ParamProcessingService;
import com.es.phoneshop.web.servlets.header.MiniCartServlet;
import com.sun.net.httpserver.HttpServer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.UnaryOperator;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;
import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.PAYMENT_METHOD;

public class AdvancedSearchPageServlet extends HttpServlet {

    private ParamProcessingService paramProcessingService;
    private ProductDao productDao;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        paramProcessingService = DefaultParamProcessingService.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> errors = getParametersErrorsFromRequest(request);
        request.setAttribute(SEARCH_ERRORS, errors);
        AdvancedProductDescription advancedProductDescription;

        if(errors.isEmpty()) {
            request.setAttribute(PRODUCTS, productDao.findProducts(getAdvancedDescription(request)));
            request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
        }
          else  doGet(request, response);

    }

    private Map<String,String> getParametersErrorsFromRequest(HttpServletRequest request){
        Map<String,String> errors = new HashMap<>();
        Locale locale = request.getLocale();
        String error = paramProcessingService.getErrorOnPrice(locale, request.getParameter(MIN_PRICE));
        if(error != null)
            errors.put(MIN_PRICE, error);
        error = paramProcessingService.getErrorOnPrice(locale, request.getParameter(MAX_PRICE));
        if(error != null)
            errors.put(MAX_PRICE, error);
        error = paramProcessingService.getErrorOnPrice(locale, request.getParameter(MIN_STOCK));
        if(error != null)
            errors.put(MIN_STOCK, error);

        return errors;
    }

    AdvancedProductDescription getAdvancedDescription(HttpServletRequest request){
        String code = request.getParameter(PRODUCT_CODE);
        BigDecimal priceMax;
        BigDecimal priceMin;
        Integer minStock;

        if (paramProcessingService.getErrorOnEmptyParameter(request.getParameter(MIN_STOCK)) != null)
            minStock = null;
        else
            minStock = paramProcessingService.getQuantityFromParam(request.getLocale(), request.getParameter(MIN_STOCK));

        priceMin = paramProcessingService.getPriceFromParam(request.getLocale(), request.getParameter(MIN_PRICE));
        priceMax = paramProcessingService.getPriceFromParam(request.getLocale(), request.getParameter(MAX_PRICE));

       return new AdvancedProductDescription (code, priceMax, priceMin, minStock);
    }


}
