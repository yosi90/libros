package com.api.books.services;

import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.askers.NewSaga;
import com.api.books.services.models.dtos.askers.ResponseDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface SagaService {

    ResponseEntity<List<SagaDTO>> getAllSagas();

    ResponseEntity<ResponseDTO> addSaga(NewSaga sagaNew);

    ResponseEntity<SagaDTO> updateName(Long id, String nameNew);
}
