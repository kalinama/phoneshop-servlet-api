package com.es.phoneshop.model.item.enums;

import com.es.phoneshop.model.item.IdentifiedItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;

import java.util.HashMap;
import java.util.Map;

public enum ItemType {

    PRODUCT(Product.class), ORDER(Order.class), ITEM(IdentifiedItem.class);

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

    public static ItemType of(Class<? extends IdentifiedItem> clazz) {
        ItemType result = map.get(clazz.getSimpleName());
        if (result == null) {
            return ITEM;
        }
        return result;
    }

    public String getClassName() {
        return className;
    }
}
