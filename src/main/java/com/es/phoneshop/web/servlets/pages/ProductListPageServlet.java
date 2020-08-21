package com.es.phoneshop.web.servlets.pages;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.enums.SortParameter;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.service.DefaultViewedProductsService;
import com.es.phoneshop.model.product.service.ViewedProductsService;
import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;

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
        String query = request.getParameter(QUERY);
        String sortParameter = request.getParameter(SORT);
        String sortOrder = request.getParameter(SORTING_ORDER);
        ViewedProductsUnit viewedProductsUnit = viewedProductsService.getViewedProductsUnit(request.getSession());

        request.setAttribute(VIEWED_PRODUCTS, viewedProductsService.getViewedProducts(viewedProductsUnit));
        request.setAttribute(PRODUCTS, productDao.findProducts(query,
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
