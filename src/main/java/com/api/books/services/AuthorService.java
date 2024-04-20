package com.api.books.services;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface AuthorService {

    ResponseEntity<ResponseDTO> addAuthor(AuthorDTO authorNew, BindingResult result);

    ResponseEntity<AuthorDTO> updateName(Long id, String nameNew);
}
