package com.api.books.services.impl;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.persistence.repositories.SagaRepository;
import com.api.books.persistence.repositories.UniverseRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.UniverseService;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.askers.NewUniverse;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedUniverseDTO;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UniverseServiceImpl implements UniverseService {

    private final UniverseRepository universeRepository;
    private final AuthorRepository authorRepository;
    private final SagaRepository sagaRepository;
    private final UserRepository userRepository;

    public UniverseServiceImpl(UniverseRepository universeRepository, AuthorRepository authorRepository, SagaRepository sagaRepository, UserRepository userRepository) {
        this.universeRepository = universeRepository;
        this.authorRepository = authorRepository;
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
    public ResponseEntity<CreatedUniverseDTO> addUniverse(NewUniverse universeNew, BindingResult result) {
        try {
            if (result != null && result.hasErrors())
                return ResponseEntity.unprocessableEntity().build();
            Optional<UniverseEntity> existingUniverse = universeRepository.findByName(universeNew.getName());
            if (existingUniverse.isPresent() && Objects.equals(universeNew.getUserId(), existingUniverse.get().getUserUniverses().getId()))
                return new ResponseEntity<>(new CreatedUniverseDTO(), HttpStatus.CONFLICT);
            UniverseEntity universeTEMP = getTemplateUniverse();
            UniverseEntity universeEntity = updateTemplateUniverse(universeTEMP, universeNew).orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            return ResponseEntity.ok(universeEntity.toCDTO());
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
            List<AuthorEntity> authors = new ArrayList<>();
            for(AuthorDTO authorDTO: updatedUniverse.getAuthors()) {
                AuthorEntity author = authorRepository.findById(authorDTO.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
                authors.add(author);
            }
            universeTemplate.setAuthorsUniverses(authors);
            SagaEntity saga = new SagaEntity();
            saga.setName("Sin saga");
            saga.setAuthorsSagas(authors);
            saga.setUniverseSagas(universeTemplate);
            saga.setUserSagas(user);
            saga = sagaRepository.save(saga);
            universeTemplate.addSaga(saga);
            UniverseEntity universeFinal = universeRepository.save(universeTemplate);
            for(AuthorEntity author: authors) {
                List<UniverseEntity> authorUniverses = author.getUniversesAuthors();
                authorUniverses.add(universeFinal);
                author.setUniversesAuthors(authorUniverses);
                List<SagaEntity> authorSagas = author.getSagasAuthors();
                authorSagas.add(saga);
                author.setSagasAuthors(authorSagas);
                authorRepository.save(author);
            }
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
