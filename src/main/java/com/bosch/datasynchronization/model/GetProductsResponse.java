package com.bosch.datasynchronization.model;

import java.util.List;

public class GetProductsResponse implements FruitShopResponse<Product>{
    private Meta meta;
    private List<Product> products;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getData() {
        return getProducts();
    }
}
