package com.es.phoneshop.web.productDetails;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart());
        Long id = getProductIdFromRequest(request);
        request.setAttribute("product", productDao.getProduct(id));
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request,response);
    }

    private Long getProductIdFromRequest(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(isPostRequestCorrect(request)) {
            Long id = getProductIdFromRequest(request);
            response.sendRedirect(request.getContextPath() + "/products/"
                    + id + "?message=Added to cart successfully");
        }
        else
            doGet(request,response);
    }

    private boolean isPostRequestCorrect(HttpServletRequest request) {
        int quantity;
        double quantityDouble;
        Long id = getProductIdFromRequest(request);

        try {
            Locale locale = request.getLocale();
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            quantityDouble = numberFormat.parse(request.getParameter("quantity")).doubleValue();
            quantity = (int)quantityDouble;
        }
        catch (ParseException e) {
            request.setAttribute("addToCartError", "Not a number");
            return false;
        }

        if (quantityDouble != quantity){
            request.setAttribute("addToCartError", "Can't enter fractional number");
            return false;
        }

        if (quantity == 0) {
            request.setAttribute("addToCartError", "Can't add 0 items");
            return false;
        }

        try {
            cartService.add(id, quantity);
        }
        catch (OutOfStockException e) {
            request.setAttribute("addToCartError", "Not enough stock. Available: " + e.getAvailableStock());
            return false;
        }

        return true;
    }

}
