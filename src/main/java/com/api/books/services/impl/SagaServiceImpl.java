package com.api.books.services.impl;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.persistence.repositories.SagaRepository;
import com.api.books.persistence.repositories.UniverseRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.SagaService;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.templates.NewSaga;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SagaServiceImpl implements SagaService {

    private final SagaRepository sagaRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final UniverseRepository universeRepository;

    public SagaServiceImpl(SagaRepository sagaRepository, UserRepository userRepository, AuthorRepository authorRepository, UniverseRepository universeRepository) {
        this.sagaRepository = sagaRepository;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.universeRepository = universeRepository;
    }

    @Override
    public ResponseEntity<List<SagaDTO>> getAllSagas() {
        try {
            List<SagaEntity> sagas = sagaRepository.findAll();
            List<SagaDTO> sagaDTOs = new ArrayList<>();
            if (sagas.isEmpty()) return ResponseEntity.noContent().build();
            for (SagaEntity sagaEntity : sagas)
                sagaDTOs.add(sagaEntity.toDTO());
            return ResponseEntity.ok(sagaDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> addSaga(NewSaga sagaNew) {
        ResponseDTO response = new ResponseDTO();
        try {
            Optional<SagaEntity> existingSaga = sagaRepository.findByName(sagaNew.getName());
            if (existingSaga.isPresent() && Objects.equals(sagaNew.getUserId(), existingSaga.get().getUserSagas().getId())) {
                response.newError("Nombre en uso, por favor elija otro");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            SagaEntity sagaTEMP = getTemplateSaga();
            updateTemplateSaga(sagaTEMP, sagaNew).orElseThrow(() -> new EntityNotFoundException("Saga no encontrada"));
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
            return sagaRepository.save(sagaEntity);
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
            AuthorEntity author = authorRepository.findByName(updatedSaga.getAuthor()).orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
            List<AuthorEntity> authors = new ArrayList<>();
            authors.add(author);
            sagaTemplate.setAuthorsSagas(authors);
            UniverseEntity universe = universeRepository.findByName(updatedSaga.getUniverse()).orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            sagaTemplate.setUniverseSagas(universe);
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
