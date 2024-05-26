package com.bosch.datasynchronization.repository;

import com.bosch.datasynchronization.model.EnrichedProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EnrichedProductRepository extends MongoRepository<EnrichedProduct, Integer> {
    @Query("{ 'parentId': { $exists: true, $ne: null } }")
    List<EnrichedProduct> findAllWithParents();

    @Query("{ 'parentId': { $exists: false } }")
    List<EnrichedProduct> findAllWithoutParents();
}
