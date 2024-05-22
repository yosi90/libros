package com.api.books.services;

import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.askers.NewBook;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedBookDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    ResponseEntity<BookDTO> getBookById(Long bookId, Long userId);

    ResponseEntity<List<BookDTO>> getAllBooks();

    ResponseEntity<CreatedBookDTO> addBook(NewBook bookNew, MultipartFile file);

    ResponseEntity<BookDTO> updateCover(Long bookId, MultipartFile img);

    ResponseEntity<BookDTO> updateStatus(Long bookId, Long statusId);
}
