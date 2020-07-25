package com.es.phoneshop.web.productDetails;

import com.es.phoneshop.model.product.service.DefaultProductService;
import com.es.phoneshop.model.product.service.ProductService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {

    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        request.setAttribute("product", productService.getProduct(Long.valueOf(pathInfo.substring(1))));
        request.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(request,response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = DefaultProductService.getInstance();
    }

}
