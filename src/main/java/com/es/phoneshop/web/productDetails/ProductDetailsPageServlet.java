package com.es.phoneshop.web.productDetails;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.service.DefaultProductService;
import com.es.phoneshop.model.product.service.ProductService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProductDetailsPageServlet extends HttpServlet {

    private CartService cartService;
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        productService = DefaultProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = getProductIdFromRequest(request);
        HttpSession session = request.getSession();
        ViewedProductsUnit viewedProductsUnit = productService.getViewedProductsUnit(session);
        productService.addProductToViewed(viewedProductsUnit, id);

        request.setAttribute("cart", cartService.getCart(session));
        request.setAttribute("viewedProducts", viewedProductsUnit.getViewedProductsWithoutLast());
        request.setAttribute("product", productService.getProduct(id));
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
        } catch (ParseException e) {
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
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart,id, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("addToCartError", "Not enough stock. Available: " + e.getAvailableStock());
            return false;
        }

        return true;
    }

}
