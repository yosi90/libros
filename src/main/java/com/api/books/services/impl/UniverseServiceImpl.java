package com.api.books.services.impl;

import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.SagaRepository;
import com.api.books.persistence.repositories.UniverseRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.UniverseService;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.templates.NewUniverse;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UniverseServiceImpl implements UniverseService {

    private final UniverseRepository universeRepository;
    private final SagaRepository sagaRepository;
    private final UserRepository userRepository;

    @Autowired
    public UniverseServiceImpl(UniverseRepository universeRepository, SagaRepository sagaRepository, UserRepository userRepository) {
        this.universeRepository = universeRepository;
        this.sagaRepository = sagaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<UniverseDTO>> getAllUniverses() {
        try {
            List<UniverseEntity> universes = universeRepository.findAll();
            List<UniverseDTO> universeDTOs = new ArrayList<>();
            if (universes.isEmpty()) return ResponseEntity.noContent().build();
            for (UniverseEntity universeEntity : universes)
                universeDTOs.add(universeEntity.toDTO());
            return ResponseEntity.ok(universeDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> addUniverse(NewUniverse universeNew, BindingResult result) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (result != null && result.hasErrors()) {
                for (FieldError error : result.getFieldErrors())
                    response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
            Optional<UniverseEntity> existingUniverse = universeRepository.findByName(universeNew.getName());
            if (existingUniverse.isPresent() && Objects.equals(universeNew.getUserId(), existingUniverse.get().getUserUniverses().getId())) {
                response.newError("Nombre en uso, por favor elija otro");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            UniverseEntity universeTEMP = getTemplateUniverse();
            updateTemplateUniverse(universeTEMP, universeNew).orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            response.newMessage("Usuario creado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public UniverseEntity addUniverse(NewUniverse universeNew) {
        try {
            UniverseEntity universeTEMP = getTemplateUniverse();
            return updateTemplateUniverse(universeTEMP, universeNew).orElseThrow(() -> new EntityNotFoundException("El universo no puedo ser creado"));
        } catch (Exception e) {
            return new UniverseEntity();
        }
    }

    private UniverseEntity getTemplateUniverse() {
        Optional<UniverseEntity> universeTEMP = universeRepository.findByName("universeTemplate");
        if (universeTEMP.isEmpty()) {
            UniverseEntity universeEntity = new UniverseEntity();
            universeEntity.setName("universeTemplate");
            universeEntity.setAuthorsUniverses(new ArrayList<>());
            universeEntity.setBooksUniverse(new ArrayList<>());
            universeEntity.setSagasUniverse(new ArrayList<>());
            universeEntity = universeRepository.save(universeEntity);
            return universeEntity;
        }
        return universeTEMP.get();
    }

    private Optional<UniverseEntity> updateTemplateUniverse(UniverseEntity universeTemplate, NewUniverse updatedUniverse) {
        try {
            universeTemplate.setName(updatedUniverse.getName());
            UserEntity user = userRepository.findById(updatedUniverse.getUserId()).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            universeTemplate.setUserUniverses(user);
            universeTemplate.setAuthorsUniverses(updatedUniverse.getAuthorEntities());
            SagaEntity saga = new SagaEntity();
            saga.setName("Sin saga");
            saga.setAuthorsSagas(updatedUniverse.getAuthorEntities());
            saga.setUniverseSagas(universeTemplate);
            saga = sagaRepository.save(saga);
            universeTemplate.addSaga(saga);
            UniverseEntity universeFinal = universeRepository.save(universeTemplate);
            return Optional.of(universeFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<UniverseDTO> updateName(Long id, String nameNew) {
        try {
            UniverseEntity previousUniverse = universeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            previousUniverse.setName(nameNew);
            final UniverseEntity universe = universeRepository.save(previousUniverse);
            return ResponseEntity.ok(universe.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
