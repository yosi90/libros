package com.api.books.services;

import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.askers.NewAuthor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface AuthorService {

    ResponseEntity<List<AuthorDTO>> getAllAuthors();

    ResponseEntity<AuthorDTO> addAuthor(NewAuthor authorNew, BindingResult result);

    ResponseEntity<AuthorDTO> updateName(Long id, String nameNew);
}
