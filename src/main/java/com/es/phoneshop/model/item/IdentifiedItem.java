package com.es.phoneshop.model.item;

public abstract class IdentifiedItem {
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
