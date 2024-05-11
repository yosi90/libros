package com.api.books.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.api.books.services.models.dtos.BookstatusDTO;

public interface BookStatusService {

    ResponseEntity<BookstatusDTO> getStatusById(Long statusId);

    ResponseEntity<List<BookstatusDTO>> getAllStatuses();
}
