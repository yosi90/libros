package com.api.books.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.books.persistence.entities.ReadStatusEntity;

public interface ReadStatusRepository extends JpaRepository<ReadStatusEntity, Long> {

}