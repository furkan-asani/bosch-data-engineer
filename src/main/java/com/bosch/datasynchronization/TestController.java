package com.bosch.datasynchronization;

import com.bosch.datasynchronization.client.FruitShopRestClient;
import com.bosch.datasynchronization.model.Product;
import com.bosch.datasynchronization.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private FruitShopRestClient fruitShopRestClient;

    @GetMapping("test-fruit-shop-api")
    public List<Product> getAllProducts() {
        return fruitShopRestClient.getAllProducts(null);
    }

    @GetMapping("vendors")
    public List<Vendor> getAllVendors() {
        return fruitShopRestClient.getAllVendors();
    }
}
