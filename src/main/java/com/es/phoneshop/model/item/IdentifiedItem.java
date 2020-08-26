package com.es.phoneshop.model.item;

import java.io.Serializable;

public abstract class IdentifiedItem implements Serializable{
    private Long id;

    public IdentifiedItem(){}

    public IdentifiedItem(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
