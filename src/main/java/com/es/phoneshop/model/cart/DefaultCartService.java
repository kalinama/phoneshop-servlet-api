package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class DefaultCartService implements CartService{

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static class DefaultCartServiceHolder {
        public static final DefaultCartService HOLDER_INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.DefaultCartServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public Cart getCart(HttpSession httpSession) {
        synchronized (httpSession) {
            Cart cart = (Cart) httpSession.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                httpSession.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            if (quantity == 0) return;

            Product product = productDao.getProduct(productId);
            Optional<CartItem> existedCartItem = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findAny();

            if (!existedCartItem.isPresent()) {
                CartItem newCartItem = new CartItem(product, 0);
                increaseCartItemQuantity(newCartItem, quantity);
                cart.getItems().add(newCartItem);
            } else
                increaseCartItemQuantity(existedCartItem.get(), quantity);
        }
    }

    private void increaseCartItemQuantity(CartItem cartItem, int quantity) throws OutOfStockException {
        int availableQuantity = cartItem.getProduct().getStock() - cartItem.getQuantity();

        if (quantity <= availableQuantity)
            cartItem.addQuantity(quantity);
        else
            throw new OutOfStockException(availableQuantity);
    }

}
