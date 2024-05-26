package com.api.books.services;

import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.askers.NewSaga;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedSagaDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface SagaService {
    
    ResponseEntity<CreatedSagaDTO> getCreatedSagaById(Long sagaId, Long userId);

    ResponseEntity<List<SagaDTO>> getAllSagas();

    ResponseEntity<CreatedSagaDTO> addSaga(NewSaga sagaNew);

    ResponseEntity<SagaDTO> updateName(Long id, String nameNew);
}
