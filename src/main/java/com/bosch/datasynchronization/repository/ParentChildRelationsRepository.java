package com.bosch.datasynchronization.repository;

import com.bosch.datasynchronization.model.ParentChildRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentChildRelationsRepository extends JpaRepository<ParentChildRelation, Integer> {
}
