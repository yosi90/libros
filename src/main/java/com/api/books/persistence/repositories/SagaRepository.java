package com.api.books.persistence.repositories;

import com.api.books.persistence.entities.SagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SagaRepository extends JpaRepository<SagaEntity, Long> {

    Optional<SagaEntity> findByName(String name);
    
    List<SagaEntity> findByUserSagasId(Long userId);
}
