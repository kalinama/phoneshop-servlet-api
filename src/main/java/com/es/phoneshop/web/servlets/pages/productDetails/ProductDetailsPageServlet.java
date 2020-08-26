package com.es.phoneshop.web.servlets.pages.productDetails;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ViewedProductsService viewedProductsService;
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        viewedProductsService = DefaultViewedProductsService.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = getProductIdFromRequest(request);
        HttpSession session = request.getSession();
        ViewedProductsUnit viewedProductsUnit = viewedProductsService.getViewedProductsUnit(session);
        viewedProductsService.addProductToViewed(viewedProductsUnit, id);

        request.setAttribute(PRODUCT, productDao.getById(id));
        request.setAttribute(VIEWED_PRODUCTS, viewedProductsService.getViewedProductsWithoutLast(viewedProductsUnit));
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request,response);
    }

    private Long getProductIdFromRequest(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }

}
