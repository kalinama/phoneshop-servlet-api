package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.SortParameter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productsDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortParameter = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("products", productsDao.findProducts(query,
                sortParameter!=null ? SortParameter.valueOf(sortParameter) : null,
                sortOrder!=null ? SortOrder.valueOf(sortOrder) : null));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productsDao = new ArrayListProductDao(true);
    }
}
