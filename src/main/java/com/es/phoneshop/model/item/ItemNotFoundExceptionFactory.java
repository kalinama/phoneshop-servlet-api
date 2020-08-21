package com.es.phoneshop.model.item;

import com.es.phoneshop.model.item.enums.ItemType;
import com.es.phoneshop.model.item.exception.ItemNotFoundException;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

public class ItemNotFoundExceptionFactory {

    public static ItemNotFoundException getException(ItemType itemType, Long id){
        switch (itemType) {
            case PRODUCT:
                return new ProductNotFoundException(id.toString());
            case ORDER:
                return new OrderNotFoundException(id.toString());

            default:
                throw new IllegalArgumentException("Wrong item type:" + itemType);
        }
    }
}
