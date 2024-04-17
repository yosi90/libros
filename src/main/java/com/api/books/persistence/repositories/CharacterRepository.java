package com.api.books.persistence.repositories;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {
    Optional<CharacterEntity> findByName(String name);
}
