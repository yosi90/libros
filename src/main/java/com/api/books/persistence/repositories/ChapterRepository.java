package com.api.books.persistence.repositories;

import com.api.books.persistence.entities.ChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<ChapterEntity, Long> {

    Optional<ChapterEntity> findByName(String name);
}
