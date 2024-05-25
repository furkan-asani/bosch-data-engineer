package com.bosch.datasynchronization.model;

import org.springframework.data.annotation.Id;

public class Vendor {
    @Id
    private String _id;
    private String _name;
    private String _selfLink;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getSelfLink() {
        return _selfLink;
    }

    public void setSelfLink(String _selfLink) {
        this._selfLink = _selfLink;
    }
}
