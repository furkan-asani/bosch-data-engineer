package com.bosch.datasynchronization;

import com.bosch.datasynchronization.client.FruitShopRestClient;
import com.bosch.datasynchronization.repository.EnrichedProductRepository;
import com.bosch.datasynchronization.repository.ParentChildRelationsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DataSynchronizerTest {

    @Autowired
    private DataSynchronizer dataSynchronizer;

    @Autowired
    private FruitShopRestClient fruitShopRestClient;

    @Autowired
    private ParentChildRelationsRepository parentChildRelationShipRepository;

    @Autowired
    private EnrichedProductRepository enrichedProductRepository;

    @Test
    void testHappyPath() {
        //given

        //when
        dataSynchronizer.synchronizeData();

        //then
    }
}