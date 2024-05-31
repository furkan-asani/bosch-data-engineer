package com.bosch.datasynchronization;

import com.bosch.datasynchronization.client.FruitShopRestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FruitShopRestClientTest {

    @Autowired
    private FruitShopRestClient fruitShopRestClient;

    @Test
    void testFetchImage() {
        fruitShopRestClient.getImageForProduct(1);
    }
}