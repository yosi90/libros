package com.api.books.services;

import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.templates.NewSaga;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface SagaService {

    ResponseEntity<ResponseDTO> addSaga(NewSaga sagaNew, BindingResult result);

    ResponseEntity<SagaDTO> updateName(Long id, String nameNew);
}
