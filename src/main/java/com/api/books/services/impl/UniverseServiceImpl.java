package com.api.books.services.impl;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.persistence.repositories.BookRepository;
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
    private final BookRepository bookRepository;

    public UniverseServiceImpl(UniverseRepository universeRepository, AuthorRepository authorRepository,
            SagaRepository sagaRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.universeRepository = universeRepository;
        this.authorRepository = authorRepository;
        this.sagaRepository = sagaRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseEntity<CreatedUniverseDTO> getCreatedUniverseById(Long universeId, Long userId) {
        try {
            UniverseEntity universe = universeRepository.findById(universeId)
                    .orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            if (!Objects.equals(universe.getUserUniverses().getId(), userId))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(universe.toCDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<UniverseDTO>> getAllUniverses() {
        try {
            List<UniverseEntity> universes = universeRepository.findAll();
            List<UniverseDTO> universeDTOs = new ArrayList<>();
            if (universes.isEmpty())
                return ResponseEntity.noContent().build();
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
            if (existingUniverse.isPresent()
                    && Objects.equals(universeNew.getUserId(), existingUniverse.get().getUserUniverses().getId()))
                return new ResponseEntity<>(new CreatedUniverseDTO(), HttpStatus.CONFLICT);
            UniverseEntity universeTEMP = getTemplateUniverse();
            UniverseEntity universeEntity = updateTemplateUniverse(universeTEMP, universeNew)
                    .orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            return ResponseEntity.ok(universeEntity.toCDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public UniverseEntity addUniverse(NewUniverse universeNew) {
        try {
            UniverseEntity universeTEMP = getTemplateUniverse();
            return updateTemplateUniverse(universeTEMP, universeNew)
                    .orElseThrow(() -> new EntityNotFoundException("El universo no puedo ser creado"));
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

    private Optional<UniverseEntity> updateTemplateUniverse(UniverseEntity universeTemplate,
            NewUniverse updatedUniverse) {
        try {
            universeTemplate.setName(updatedUniverse.getName());
            UserEntity user = userRepository.findById(updatedUniverse.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            universeTemplate.setUserUniverses(user);
            List<AuthorEntity> authors = new ArrayList<>();
            for (AuthorDTO authorDTO : updatedUniverse.getAuthors()) {
                AuthorEntity author = authorRepository.findById(authorDTO.getAuthorId())
                        .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
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
            for (AuthorEntity author : authors) {
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
    public ResponseEntity<CreatedUniverseDTO> updateUniverse(NewUniverse universeNew, Long universeId) {
        UniverseEntity universe = universeRepository.findById(universeId)
                .orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
        universe.setName(universeNew.getName());
        List<AuthorEntity> updatedAuthorList = new ArrayList<>();
        for (AuthorDTO authorDTO : universeNew.getAuthors()) {
            AuthorEntity author = authorRepository.findById(authorDTO.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
            updatedAuthorList.add(author);
        }
        List<AuthorEntity> addedAuthors = new ArrayList<>();
        for (AuthorEntity author : updatedAuthorList) {
            if (!universe.getAuthorsUniverses().contains(author))
                addedAuthors.add(author);
        }
        List<AuthorEntity> removedAuthors = new ArrayList<>();
        for (AuthorEntity author : universe.getAuthorsUniverses()) {
            if (!updatedAuthorList.contains(author))
                removedAuthors.add(author);
        }
        UniverseEntity noUniverse = universeRepository
                .findByNameAndUserUniversesId("Sin universo", universeNew.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
        SagaEntity noSaga = sagaRepository
                .findByNameAndUserSagasIdAndUniverseSagasId("Sin saga", universeNew.getUserId(), noUniverse.getId())
                .orElseThrow(() -> new EntityNotFoundException("Saga no encontrada"));
        for (AuthorEntity author : removedAuthors) {
            List<SagaEntity> sagas = author.getSagasAuthors();
            List<SagaEntity> updatedSagas = new ArrayList<>();
            for (SagaEntity saga : sagas) {
                List<AuthorEntity> sagaAuthors = saga.getAuthorsSagas();
                List<AuthorEntity> updatedSagaAuthors = new ArrayList<>();
                if (saga.getUniverseSagas().getId() == universeId) {
                    for (AuthorEntity authorEntity : sagaAuthors) {
                        if (!removedAuthors.contains(authorEntity))
                            updatedSagaAuthors.add(authorEntity);
                    }
                    saga.setAuthorsSagas(updatedSagaAuthors);
                    SagaEntity sagaFinal = sagaRepository.save(saga);
                    updatedSagas.add(sagaFinal);
                } else
                    updatedSagas.add(saga);
            }
            author.setSagasAuthors(updatedSagas);
            List<BookEntity> books = author.getBooksAuthors();
            List<BookEntity> updatedBooks = new ArrayList<>();
            for (BookEntity book : books) {
                if (book.getUniverseBooks().getId() == universeId) {
                    book.setUniverseBooks(noUniverse);
                    book.setSagaBooks(noSaga);
                    book.setOrderInSaga(-1);
                    BookEntity bookFinal = bookRepository.save(book);
                    updatedBooks.add(bookFinal);
                } else
                    updatedBooks.add(book);
            }
            author.setBooksAuthors(updatedBooks);
            authorRepository.save(author);
        }
        universe.setAuthorsUniverses(updatedAuthorList);
        UniverseEntity universeFinal = universeRepository.save(universe);
        for (AuthorEntity author : addedAuthors) {
            List<UniverseEntity> authorUniverses = author.getUniversesAuthors();
            authorUniverses.add(universeFinal);
            author.setUniversesAuthors(authorUniverses);
            authorRepository.save(author);
        }
        for (AuthorEntity author : removedAuthors) {
            List<UniverseEntity> authorUniverses = new ArrayList<>();
            for (UniverseEntity universeEntity : author.getUniversesAuthors()) {
                if (universe.getId() != universeId)
                    authorUniverses.add(universeEntity);
            }
            author.setUniversesAuthors(authorUniverses);
            List<SagaEntity> authorSagas = new ArrayList<>();
            for (SagaEntity sagaEntity : author.getSagasAuthors()) {
                if (sagaEntity.getUniverseSagas().getId() != universeId)
                    authorSagas.add(sagaEntity);
            }
            author.setSagasAuthors(authorSagas);
            authorRepository.save(author);
        }
        List<SagaEntity> universeSagas = universeFinal.getSagasUniverse();
        for (SagaEntity sagaEntity : universeSagas) {
            if (sagaEntity.getAuthorsSagas().isEmpty()) {
                sagaEntity.setAuthorsSagas(updatedAuthorList);
                sagaRepository.save(sagaEntity);
            }
        }
        List<SagaEntity> universeSagasCopy = new ArrayList<>(universeSagas);
        for (SagaEntity sagaEntity : universeSagasCopy) {
            for (AuthorEntity authorEntity : updatedAuthorList) {
                List<SagaEntity> authorSagas = authorEntity.getSagasAuthors();
                if (authorSagas == null)
                    authorSagas = new ArrayList<>();
                authorSagas.add(sagaEntity);
                authorEntity.setSagasAuthors(authorSagas);
                authorRepository.save(authorEntity);
            }
        }
        for (BookEntity bookEntity : universeFinal.getBooksUniverse()) {
            if (bookEntity.getAuthorsBooks().isEmpty()) {
                bookEntity.setAuthorsBooks(updatedAuthorList);
                bookRepository.save(bookEntity);
            }
        }
        return ResponseEntity.ok(universeFinal.toCDTO());
    }

    @Override
    public ResponseEntity<UniverseDTO> updateName(Long id, String nameNew) {
        try {
            UniverseEntity previousUniverse = universeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            previousUniverse.setName(nameNew);
            final UniverseEntity universe = universeRepository.save(previousUniverse);
            return ResponseEntity.ok(universe.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
