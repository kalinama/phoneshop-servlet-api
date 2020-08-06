package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.exceptions.WrongItemQuantityException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class DefaultCartServiceHolder {
        static final DefaultCartService HOLDER_INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public Cart getCart(HttpSession httpSession) {
        final Object lock = httpSession.getId().intern();
        synchronized (lock) {
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
            if (quantity <=0 ) throw new WrongItemQuantityException();

            Product product = productDao.getProduct(productId);
            Optional<CartItem> existedCartItem = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findAny();

            int availableQuantity = product.getStock() -
                    existedCartItem.map(CartItem::getQuantity).orElse(0);

            if (quantity > availableQuantity)
                throw new OutOfStockException(availableQuantity);

            if (!existedCartItem.isPresent())
                cart.getItems().add(new CartItem(product, quantity));
            else
                increaseCartItemQuantity(existedCartItem.get(), quantity);
        }
    }

    private void increaseCartItemQuantity(CartItem cartItem, int quantity) {
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            if (quantity <= 0) throw new WrongItemQuantityException();

            Product product = productDao.getProduct(productId);

            if (quantity > product.getStock())
                throw new OutOfStockException(product.getStock());

            cart.getItems().stream()
                    .filter(item -> item.getProduct().equals(product))
                    .findAny().get().setQuantity(quantity);
        }
    }
}
