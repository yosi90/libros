package com.api.books.persistence.repositories;

import com.api.books.persistence.entities.BookEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByName(String name);

    List<BookEntity> findByOwnerId(Long userId);
}
