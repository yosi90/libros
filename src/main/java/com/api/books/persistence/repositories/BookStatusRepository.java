package com.api.books.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.books.persistence.entities.BookStatusEntity;

public interface BookStatusRepository extends JpaRepository<BookStatusEntity, Long> {

    Optional<BookStatusEntity> findByName(String name);
}
