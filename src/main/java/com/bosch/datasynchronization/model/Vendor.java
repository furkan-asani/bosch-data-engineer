package com.bosch.datasynchronization.model;

import org.springframework.data.annotation.Id;

public class Vendor {
    @Id
    private String id;
    private String name;
    private String selfLink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String _selfLink) {
        this.selfLink = selfLink;
    }
}
