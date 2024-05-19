package com.api.books.services.impl;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.BookStatusEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.BookStatusRepository;
import com.api.books.persistence.repositories.SagaRepository;
import com.api.books.persistence.repositories.UniverseRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.BookService;
import com.api.books.services.ImageService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.askers.NewBook;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedBookDTO;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final UniverseRepository universeRepository;
    private final SagaRepository sagaRepository;
    private final BookStatusRepository bookStatusRepository;
    private final ImageService imageService;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository, AuthorRepository authorRepository, 
    UniverseRepository universeRepository, SagaRepository sagaRepository, BookStatusRepository bookStatusRepository, ImageService imageService) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.universeRepository = universeRepository;
        this.sagaRepository = sagaRepository;
        this.bookStatusRepository = bookStatusRepository;
        this.imageService = imageService;
    }

    @Override
    public ResponseEntity<BookDTO> getBookById(Long bookId, Long userId) {
        try {
            BookEntity book = bookRepository.findById(bookId).orElse(null);
            if (book == null) return ResponseEntity.notFound().build();
            if (!Objects.equals(book.getOwner().getId(), userId)) return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(book.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        try {
            List<BookEntity> books = bookRepository.findAll();
            List<BookDTO> bookDTOS = new ArrayList<>();
            if (books.isEmpty()) return ResponseEntity.noContent().build();
            for (BookEntity bookEntity : books)
                bookDTOS.add(bookEntity.toDTO());
            return ResponseEntity.ok(bookDTOS);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<CreatedBookDTO> addBook(NewBook bookNew, MultipartFile cover) {
        try {
            Optional<BookEntity> existingBook = bookRepository.findByName(bookNew.getName());
            if (existingBook.isPresent() && Objects.equals(existingBook.get().getOwner().getId(), bookNew.getUserId()))
                return new ResponseEntity<>(new CreatedBookDTO(), HttpStatus.CONFLICT);
            BookEntity bookTEMP = getTemplateBook();
            Optional<BookEntity> bookOPT = updateTemplateBook(bookTEMP, bookNew, cover);
            if (bookOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            return ResponseEntity.ok(bookOPT.get().toCDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private BookEntity getTemplateBook() {
        Optional<BookEntity> bookTEMP = bookRepository.findByName("bookTemplate");
        if (bookTEMP.isEmpty()) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setName("bookTemplate");
            bookEntity.setAuthorsBooks(new ArrayList<>());
            bookEntity = bookRepository.save(bookEntity);
            return bookEntity;
        }
        return bookTEMP.get();
    }

    private Optional<BookEntity> updateTemplateBook(BookEntity bookTemplate, NewBook updatedBook, MultipartFile cover) {
        try {
            bookTemplate.setName(updatedBook.getName());
            UserEntity user = userRepository.findById(updatedBook.getUserId()).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            bookTemplate.setOwner(user);
            List<AuthorEntity> authors = new ArrayList<>();
            for (Long authorId : updatedBook.getAuthorIds()) {
                AuthorEntity author = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
                authors.add(author);
            }
            bookTemplate.setAuthorsBooks(authors);
            UniverseEntity universe = universeRepository.findById(updatedBook.getUniverseId()).orElseThrow(() -> new EntityNotFoundException("Universo no encontrado"));
            bookTemplate.setUniverseBooks(universe);
            SagaEntity saga = sagaRepository.findById(updatedBook.getSagaId()).orElseThrow(() -> new EntityNotFoundException("Saga no encontrada"));
            bookTemplate.setSagaBooks(saga);
            BookStatusEntity status = bookStatusRepository.findByName(updatedBook.getStatus()).orElseThrow(() -> new EntityNotFoundException("Estado de lectura no encontrado"));
            bookTemplate.setStatusBooks(status);
            String coverPath = imageService.saveImage(cover, user.getId());
            bookTemplate.setCover(coverPath);
            BookEntity bookFinal = bookRepository.save(bookTemplate);
            for (AuthorEntity author : authors) {
                List<BookEntity> authorBooks = author.getBooksAuthors();
                authorBooks.add(bookFinal);
                author.setBooksAuthors(authorBooks);
                authorRepository.save(author);
            }
            List<BookEntity> universeBooks = universe.getBooksUniverse();
            universeBooks.add(bookFinal);
            universe.setBooksUniverse(universeBooks);
            universeRepository.save(universe);
            List<BookEntity> sagaBooks = saga.getBooksSagas();
            sagaBooks.add(bookFinal);
            saga.setBooksSagas(sagaBooks);
            sagaRepository.save(saga);
            return Optional.of(bookFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<BookDTO> updateCover(Long bookId, MultipartFile img) {
        try {
            Optional<BookEntity> bookOPT = bookRepository.findById(bookId);
            if (bookOPT.isEmpty())
                return ResponseEntity.notFound().build();
            byte[] imageBytes = img.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            BookEntity book = bookOPT.get();
            book.setCover(base64Image);
            book = bookRepository.save(book);
            return ResponseEntity.ok(book.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
