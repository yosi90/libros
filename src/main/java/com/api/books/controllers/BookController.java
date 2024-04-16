package com.api.books.controllers;

import com.api.books.services.BookService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.templates.NewBook;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid NewBook bookNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new BookDTO(), HttpStatus.NOT_ACCEPTABLE);
        return bookService.addBook(bookNew);
    }

    @PatchMapping("/{bookId}/cover")
    public ResponseEntity<BookDTO> updateCover(@PathVariable Long bookId, @RequestParam("cover") MultipartFile file) {
        return bookService.updateCover(bookId, file);
    }
}
