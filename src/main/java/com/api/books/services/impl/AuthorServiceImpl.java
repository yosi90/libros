package com.api.books.services.impl;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.AuthorService;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.askers.NewAuthor;
import com.api.books.services.models.dtos.askers.ResponseDTO;

import jakarta.persistence.EntityNotFoundException;
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
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, UserRepository userRepository) {
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        try {
            List<AuthorEntity> authors = authorRepository.findAll();
            List<AuthorDTO> authorDTOs = new ArrayList<>();
            if (authors.isEmpty()) return ResponseEntity.noContent().build();
            for (AuthorEntity authorEntity : authors)
                authorDTOs.add(authorEntity.toDTO());
            return ResponseEntity.ok(authorDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> addAuthor(NewAuthor authorNew, BindingResult result) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (result != null && result.hasErrors()) {
                for (FieldError error : result.getFieldErrors())
                    response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
            Optional<AuthorEntity> existingAuthor = authorRepository.findByName(authorNew.getName());
            if (existingAuthor.isPresent() && Objects.equals(authorNew.getUserId(), existingAuthor.get().getUserAuthors().getId())) {
                response.newError("Nombre en uso, por favor elija otro");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            AuthorEntity authorTEMP = getTemplateAuthor();
            Optional<AuthorEntity> authorOPT = updateTemplateAuthor(authorTEMP, authorNew);
            if (authorOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            response.newMessage("Usuario creado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private AuthorEntity getTemplateAuthor() {
        Optional<AuthorEntity> authorTEMP = authorRepository.findByName("bookTemplate");
        if (authorTEMP.isEmpty()) {
            AuthorEntity authorEntity = new AuthorEntity();
            authorEntity.setName("bookTemplate");
            authorEntity = authorRepository.save(authorEntity);
            return authorEntity;
        }
        return authorTEMP.get();
    }

    private Optional<AuthorEntity> updateTemplateAuthor(AuthorEntity authorTemplate, NewAuthor updatedAuthor) {
        try {
            authorTemplate.setName(updatedAuthor.getName());
            Optional<UserEntity> user = userRepository.findById(updatedAuthor.getUserId());
            if (user.isEmpty())
                return Optional.empty();
            authorTemplate.setUserAuthors(user.get());
            AuthorEntity authorFinal = authorRepository.save(authorTemplate);
            return Optional.of(authorFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<AuthorDTO> updateName(Long id, String nameNew) {
        try {
            AuthorEntity previousAuthor = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
            previousAuthor.setName(nameNew);
            final AuthorEntity author = authorRepository.save(previousAuthor);
            return ResponseEntity.ok(author.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

