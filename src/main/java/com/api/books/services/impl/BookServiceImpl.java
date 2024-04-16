package com.api.books.services.impl;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.BookService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.templates.NewBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<BookDTO> getBookById(Long bookId) {
        try {
            BookEntity book = bookRepository.findById(bookId).orElse(null);
            if (book == null) return ResponseEntity.notFound().build();
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
    public ResponseEntity<BookDTO> addBook(NewBook bookNew) {
        try {
            Optional<BookEntity> existingBook = bookRepository.findByName(bookNew.getTitle());
            if (existingBook.isPresent())
                return new ResponseEntity<>(new BookDTO(), HttpStatus.CONFLICT);
            BookEntity bookTEMP = getTemplateBook();
            Optional<BookEntity> bookOPT = updateTemplateBook(bookTEMP, bookNew);
            if (bookOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            return ResponseEntity.ok(bookOPT.get().toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private BookEntity getTemplateBook() {
        Optional<BookEntity> bookTEMP = bookRepository.findByName("bookTemplate");
        if (bookTEMP.isEmpty()) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setName("bookTemplate");
            bookEntity.setAuthor("authorTemplate");
            bookEntity = bookRepository.save(bookEntity);
            return bookEntity;
        }
        return bookTEMP.get();
    }

    private Optional<BookEntity> updateTemplateBook(BookEntity bookTemplate, NewBook updatedBook) {
        try {
            bookTemplate.setName(updatedBook.getTitle());
            bookTemplate.setAuthor(updatedBook.getAuthor());
            Optional<UserEntity> userOPT = userRepository.findById(updatedBook.getOwnerId());
            if (userOPT.isEmpty())
                throw new Exception("Dueño no encontrado");
            bookTemplate.setOwner(userOPT.get());
            BookEntity bookFinal = bookRepository.save(bookTemplate);
            return Optional.of(bookFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /*@Override
    public ResponseEntity<BookDTO> updateCover(Long bookId, MultipartFile img) {
        try {
            Optional<BookEntity> bookOPT = bookRepository.findById(bookId);
            if(bookOPT.isEmpty())
                return ResponseEntity.notFound().build();
            Path uploadPath = Paths.get("./media/covers");
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(String.format("%d.jpg", bookId));
            Files.copy(img.getInputStream(), filePath);
            String coverUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/media/covers/{bookId}.jpg")
                    .buildAndExpand(bookId)
                    .toUriString();
            BookEntity book = bookOPT.get();
            book.setCover(coverUri);
            book = bookRepository.save(book);
            return ResponseEntity.ok(book.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }*/

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
