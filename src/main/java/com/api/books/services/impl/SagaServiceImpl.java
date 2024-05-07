package com.api.books.services.impl;

import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.SagaRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.SagaService;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.templates.NewSaga;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class SagaServiceImpl implements SagaService {

    private final SagaRepository sagaRepository;

    private final UserRepository userRepository;

    @Autowired
    public SagaServiceImpl(SagaRepository sagaRepository, UserRepository userRepository) {
        this.sagaRepository = sagaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ResponseDTO> addSaga(NewSaga sagaNew, BindingResult result) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (result != null && result.hasErrors()) {
                for (FieldError error : result.getFieldErrors())
                    response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
            Optional<SagaEntity> existingSaga = sagaRepository.findByName(sagaNew.getName());
            if (existingSaga.isPresent() && Objects.equals(sagaNew.getUserId(), existingSaga.get().getUserSagas().getId())) {
                response.newError("Nombre en uso, por favor elija otro");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            SagaEntity sagaTEMP = getTemplateSaga();
            updateTemplateSaga(sagaTEMP, sagaNew).orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            response.newMessage("Usuario creado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private SagaEntity getTemplateSaga() {
        Optional<SagaEntity> sagaTEMP = sagaRepository.findByName("sagaTemplate");
        if (sagaTEMP.isEmpty()) {
            SagaEntity sagaEntity = new SagaEntity();
            sagaEntity.setName("sagaTemplate");
            sagaEntity.setBooksSagas(new ArrayList<>());
            sagaEntity = sagaRepository.save(sagaEntity);
            return sagaEntity;
        }
        return sagaTEMP.get();
    }

    private Optional<SagaEntity> updateTemplateSaga(SagaEntity sagaTemplate, NewSaga updatedSaga) {
        try {
            sagaTemplate.setName(updatedSaga.getName());
            Optional<UserEntity> user = userRepository.findById(updatedSaga.getUserId());
            if (user.isEmpty())
                return Optional.empty();
            sagaTemplate.setUserSagas(user.get());
            sagaTemplate.setAuthorsSagas(updatedSaga.getAuthorEntities());
            SagaEntity sagaFinal = sagaRepository.save(sagaTemplate);
            return Optional.of(sagaFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<SagaDTO> updateName(Long id, String nameNew) {
        try {
            SagaEntity previousSaga = sagaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            previousSaga.setName(nameNew);
            final SagaEntity saga = sagaRepository.save(previousSaga);
            return ResponseEntity.ok(saga.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
