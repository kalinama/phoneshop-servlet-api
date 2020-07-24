package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.util.Optional;

public class DefaultCartService implements CartService{

    private Cart cart;
    private ProductDao productDao;

    private DefaultCartService() {
        cart = new Cart();
        productDao = ArrayListProductDao.getInstance();
    }

    public static class DefaultCartServiceHolder {
        public static final DefaultCartService HOLDER_INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.DefaultCartServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) throws OutOfStockException {
        if (quantity == 0) return;

        Product product = productDao.getProduct(productId);
        Optional <CartItem> existedCartItem = cart.getItems().stream()
              .filter(cartItem -> cartItem.getProduct().equals(product))
              .findAny();

        if (!existedCartItem.isPresent()) {
            CartItem newCartItem = new CartItem(product, 0);
            increaseCartItemQuantity(newCartItem, quantity);
            cart.getItems().add(newCartItem);
        } else
            increaseCartItemQuantity(existedCartItem.get(), quantity);
    }

    private void increaseCartItemQuantity(CartItem cartItem, int quantity) throws OutOfStockException {
        int availableQuantity = cartItem.getProduct().getStock() - cartItem.getQuantity();

        if (quantity <= availableQuantity)
            cartItem.addQuantity(quantity);
        else
            throw new OutOfStockException(availableQuantity);
    }

}
