package com.api.books.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.books.persistence.entities.BookStatusEntity;
import com.api.books.persistence.repositories.BookStatusRepository;
import com.api.books.services.BookStatusService;
import com.api.books.services.models.dtos.BookstatusDTO;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookStatusServiceImpl implements BookStatusService {
    
    private final BookStatusRepository bookStatusRepository;

    public BookStatusServiceImpl(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    @Override
    public ResponseEntity<BookstatusDTO> getStatusById(Long statusId) {
        try {
            BookStatusEntity status = bookStatusRepository.findById(statusId).orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));
            return ResponseEntity.ok(status.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<BookstatusDTO>> getAllStatuses() {
        try {
            List<BookStatusEntity> statuses = bookStatusRepository.findAll();
            List<BookstatusDTO> statusDTOs = new ArrayList<>();
            if (statuses.isEmpty()) return ResponseEntity.noContent().build();
            for (BookStatusEntity statusEntity : statuses)
                statusDTOs.add(statusEntity.toDTO());
            return ResponseEntity.ok(statusDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
