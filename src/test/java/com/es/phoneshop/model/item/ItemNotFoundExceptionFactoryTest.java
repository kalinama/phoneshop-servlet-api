package com.es.phoneshop.model.item;

import com.es.phoneshop.model.item.enums.ItemType;
import com.es.phoneshop.model.item.exception.ItemNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import org.junit.Test;


public class ItemNotFoundExceptionFactoryTest {

    @Test(expected = ProductNotFoundException.class)
    public void getProductException(){
         throw getException(ItemType.of(Product.class));
    }

    @Test(expected = OrderNotFoundException.class)
    public void getOrderException(){
        throw getException(ItemType.of(Order.class));
    }

    @Test(expected = ItemNotFoundException.class)
    public void getItemException(){
        throw getException(ItemType.of(IdentifiedItem.class));
    }

    private ItemNotFoundException getException(ItemType itemType){
        long id = 1L;
        return ItemNotFoundExceptionFactory.getException(itemType, id);
    }
}
