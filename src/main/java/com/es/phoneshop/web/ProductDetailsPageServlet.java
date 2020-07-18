package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productsDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        boolean showPriceHistory = pathInfo.endsWith("/price-history");

        if (showPriceHistory)
        pathInfo = Arrays.asList(request.getPathInfo().split("/price-history")).get(0);

        request.setAttribute("product", productsDao.getProduct(Long.valueOf(pathInfo.substring(1))));

        if (showPriceHistory)
            request.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(request,response);
        else
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request,response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productsDao = ArrayListProductDao.getInstance();
    }
}
