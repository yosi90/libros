package com.api.books.services;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.templates.NewBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    ResponseEntity<BookDTO> getBookById(Long bookId);

    ResponseEntity<List<BookDTO>> getAllBooks();

    ResponseEntity<BookDTO> addBook(NewBook bookNew);

    ResponseEntity<BookDTO> updateCover(Long bookId, MultipartFile img);
}
