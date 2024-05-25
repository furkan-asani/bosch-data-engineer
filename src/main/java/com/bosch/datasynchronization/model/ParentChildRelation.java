package com.bosch.datasynchronization.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ParentChildRelation {
    @Id
    private int productId;
    private int parentId;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
