package com.es.phoneshop.web.servlets.pages.productDetails;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import static com.es.phoneshop.web.constants.AttributeAndParameterConstants.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        request.setAttribute(PRODUCT, productDao.getById(Long.valueOf(pathInfo.substring(1))));
        request.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(request,response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

}
