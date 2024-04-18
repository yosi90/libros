package com.api.books.Repositories;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepoTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    @BeforeEach
    void setup() {
        var res = bookRepository.findAll().size();
        if(res > 0)
            return;
        String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (int i = 0; i < 10; i++) {
            UserEntity user = UserEntity.builder()
                    .name(String.format("Nombre%s", letras[i]))
                    .email(String.format("ant%s@onio.es", letras[i]))
                    .password("Cont@1234")
                    .lifeSpan(LocalDateTime.now())
                    .build();
            userRepository.save(user);
            BookEntity book = BookEntity.builder()
                    .name(String.format("Nombre%s", letras[i]))
                    .author(String.format("Autor%s", letras[i]))
                    .owner(user)
                    .isRead(true)
                    .chapters(new ArrayList<>())
                    .characters(new ArrayList<>())
                    .build();
            bookRepository.save(book);
        }
    }

    @Test
    @DisplayName("Recibir colección de libros")
    @Transactional
    @Order(1)
    void bookCollection() {
        var res = bookRepository.findAll();
        assertThat(res).isNotNull();
        assertThat(res).isNotEmpty();
        assertThat(res.size()).isEqualTo(10);
    }

    @RepeatedTest(3)
    @DisplayName("Recibir libro por id")
    @Transactional
    @Order(2)
    void bookById() {
        Random rnd = new Random();
        Optional<BookEntity> res = bookRepository.findById((long)(rnd.nextInt(9) + 1));
        assertThat(res).isNotNull();
    }

    @Test
    @DisplayName("Guardar nuevo libro")
    @Transactional
    @Commit
    @Order(3)
    void saveBook() {
        BookEntity book = BookEntity.builder()
                .name("Nombre")
                .author("Autor")
                .owner(userRepository.findById(1L).get())
                .isRead(true)
                .chapters(new ArrayList<>())
                .characters(new ArrayList<>())
                .build();
        BookEntity res = bookRepository.save(book);
        assertThat(res).isNotNull();
        assertThat(res.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar un libro")
    @Transactional
    @Commit
    @Order(4)
    void updateBook() {
        Optional<BookEntity> book = bookRepository.findById(1L);
        assertThat(book).isPresent();
        BookEntity bookToUpdate = book.get();
        bookToUpdate.setName("Actualizado");
        BookEntity updatedBook = bookRepository.save(bookToUpdate);
        assertThat(updatedBook.getName()).isEqualTo("Actualizado");
    }

    @Test
    @DisplayName("Eliminar un libro")
    @Transactional
    @Commit
    @Order(5)
    void removeBook() {
        bookRepository.deleteById(1L);
        Optional<BookEntity> bookVoid = bookRepository.findById(1L);
        assertThat(bookVoid).isEmpty();
    }
}
