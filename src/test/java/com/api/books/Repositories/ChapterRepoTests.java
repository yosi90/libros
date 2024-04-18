package com.api.books.Repositories;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.ChapterEntity;
import com.api.books.persistence.entities.CharacterEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.ChapterRepository;
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
public class ChapterRepoTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Transactional
    @BeforeEach
    void setup() {
        var res = bookRepository.findAll().size();
        if (res > 0)
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
            ChapterEntity chapter = ChapterEntity.builder()
                    .name(String.format("Nombre%s", letras[i]))
                    .description(String.format("Descripción%s", letras[i]))
                    .orderInBook(i)
                    .origin(book)
                    .characters(new ArrayList<>())
                    .build();
        }
    }

    @Test
    @DisplayName("Recibir colección de capítulos")
    @Transactional
    @Order(1)
    void chapterCollection() {
        var res = chapterRepository.findAll();
        assertThat(res).isNotNull();
        assertThat(res).isNotEmpty();
        assertThat(res.size()).isEqualTo(10);
    }

    @RepeatedTest(3)
    @DisplayName("Recibir capitulo por id")
    @Transactional
    @Order(2)
    void chapterById() {
        Random rnd = new Random();
        Optional<ChapterEntity> res = chapterRepository.findById((long)(rnd.nextInt(9) + 1));
        assertThat(res).isNotNull();
    }

    @Test
    @DisplayName("Guardar nuevo capítulo")
    @Transactional
    @Commit
    @Order(3)
    void saveChapter() {
        ChapterEntity chapter = ChapterEntity.builder()
                .name("Nombre")
                .description("Descripción")
                .orderInBook(1)
                .origin(bookRepository.findById(1L).get())
                .characters(new ArrayList<>())
                .build();
        ChapterEntity res = chapterRepository.save(chapter);
        assertThat(res).isNotNull();
        assertThat(res.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar un capitulo")
    @Transactional
    @Commit
    @Order(4)
    void updateChapter() {
        Optional<ChapterEntity> book = chapterRepository.findById(1L);
        assertThat(book).isPresent();
        ChapterEntity chapterToUpdate = book.get();
        chapterToUpdate.setName("Actualizado");
        ChapterEntity updatedChapter = chapterRepository.save(chapterToUpdate);
        assertThat(updatedChapter.getName()).isEqualTo("Actualizado");
    }

    @Test
    @DisplayName("Eliminar un capitulo")
    @Transactional
    @Commit
    @Order(5)
    void removeChapter() {
        chapterRepository.deleteById(1L);
        Optional<ChapterEntity> chapterVoid = chapterRepository.findById(1L);
        assertThat(chapterVoid).isEmpty();
    }
}
