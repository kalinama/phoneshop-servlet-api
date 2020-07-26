package com.es.phoneshop.web;

import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.enums.SortParameter;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.service.DefaultViewedProductsService;
import com.es.phoneshop.model.product.service.ViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {

    private ViewedProductsService viewedProductsService;
    private ProductDao productDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortParameter = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        ViewedProductsUnit viewedProductsUnit = viewedProductsService.getViewedProductsUnit(request.getSession());

        request.setAttribute("viewedProducts", viewedProductsService.getViewedProducts(viewedProductsUnit));
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortParameter).map(SortParameter::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        viewedProductsService = DefaultViewedProductsService.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

}
