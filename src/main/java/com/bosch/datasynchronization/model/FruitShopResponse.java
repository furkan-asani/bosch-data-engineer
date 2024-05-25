package com.bosch.datasynchronization.model;

import java.util.List;

public interface FruitShopResponse<E> {
    public Meta getMeta();
    public List<E> getData();
}
