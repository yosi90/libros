package com.api.books.persistence.repositories;

import com.api.books.persistence.entities.ChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<ChapterEntity, Long> {
}
