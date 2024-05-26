package com.bosch.datasynchronization.repository;

import com.bosch.datasynchronization.model.SynchronizationRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SynchronizationRunsRepository extends JpaRepository<SynchronizationRun, Integer> {
    SynchronizationRun findTopByOrderByIdDesc();
}
