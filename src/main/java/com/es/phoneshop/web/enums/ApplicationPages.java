package com.es.phoneshop.web.enums;

public enum ApplicationPages {

    PLP("/phoneshop-servlet-api/products"),
    PDP("/phoneshop-servlet-api/products/");

    private String url;

    ApplicationPages(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
