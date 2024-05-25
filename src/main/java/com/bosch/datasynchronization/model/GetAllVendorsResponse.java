package com.bosch.datasynchronization.model;

import java.util.List;

public class GetAllVendorsResponse implements FruitShopResponse<Vendor> {
    private Meta _meta;
    private List<Vendor> _vendors;

    public Meta getMeta() {
        return _meta;
    }

    public void setMeta(Meta meta) {
        _meta = meta;
    }

    public List<Vendor> getVendors() {
        return _vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        _vendors = vendors;
    }

    public List<Vendor> getData(){
        return getVendors();
    }
}
