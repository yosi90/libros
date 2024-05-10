package com.api.books.persistence.repositories;

import com.api.books.persistence.entities.UniverseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UniverseRepository extends JpaRepository<UniverseEntity, Long> {

    Optional<UniverseEntity> findByName(String name);

    Optional<UniverseEntity> findByUserUniverses_id(Long userId);
    
    List<UniverseEntity> findByUserUniversesId(Long userId);
}
