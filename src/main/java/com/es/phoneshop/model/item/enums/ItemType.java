package com.es.phoneshop.model.item.enums;

import com.es.phoneshop.model.item.IdentifiedItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;

import java.util.HashMap;
import java.util.Map;

public enum ItemType {

    PRODUCT(Product.class), ORDER(Order.class);

    private static final Map<String, ItemType> map;
    private String className;

    static {
        map = new HashMap<>(values().length, 1);
        for (ItemType type : values())
            map.put(type.className, type);
    }

    ItemType(Class<? extends IdentifiedItem> clazz){
        this.className = clazz.getSimpleName();
    }

    public static ItemType of(String className) {
        ItemType result = map.get(className);
        if (result == null) {
            throw new IllegalArgumentException("Invalid class name: " + className);
        }
        return result;
    }

    public String getClassName() {
        return className;
    }
}
