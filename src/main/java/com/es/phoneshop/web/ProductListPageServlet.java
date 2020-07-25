package com.es.phoneshop.web;

import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.enums.SortParameter;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.service.DefaultProductService;
import com.es.phoneshop.model.product.service.ProductService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {

    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortParameter = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        ViewedProductsUnit viewedProductsUnit = productService.getViewedProductsUnit(request.getSession());

        request.setAttribute("viewedProducts", viewedProductsUnit.getFixedAmountOfViewedProducts());
        request.setAttribute("products", productService.findProducts(query,
                Optional.ofNullable(sortParameter).map(SortParameter::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = DefaultProductService.getInstance();
    }

}
