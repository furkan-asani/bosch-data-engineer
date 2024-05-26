package com.bosch.datasynchronization.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
public class EnrichedProduct {

    @Id
    private String id;
    private Integer productId;
    private String  name;
    private Integer parentId;
    private List<Vendor> vendors;
    private String  hash;
    private Binary image;

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Binary getImage() {
        return image;
    }

    public void setImage(Binary image) {
        this.image = image;
    }
}