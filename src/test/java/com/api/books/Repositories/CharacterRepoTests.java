package com.api.books.Repositories;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.ChapterEntity;
import com.api.books.persistence.entities.CharacterEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.ChapterRepository;
import com.api.books.persistence.repositories.CharacterRepository;
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
public class CharacterRepoTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CharacterRepository characterRepository;

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
            CharacterEntity character = CharacterEntity.builder()
                    .name(String.format("Nombre%s", letras[i]))
                    .description(String.format("Descripción%s", letras[i]))
                    .book(book)
                    .build();
        }
    }

    @Test
    @DisplayName("Recibir colección de personajes")
    @Transactional
    @Order(1)
    void characterCollection() {
        var res = characterRepository.findAll();
        assertThat(res).isNotNull();
        assertThat(res).isNotEmpty();
        assertThat(res.size()).isEqualTo(10);
    }

    @RepeatedTest(3)
    @DisplayName("Recibir personaje por id")
    @Transactional
    @Order(2)
    void characterById() {
        Random rnd = new Random();
        Optional<CharacterEntity> res = characterRepository.findById((long)(rnd.nextInt(9) + 1));
        assertThat(res).isNotNull();
    }

    @Test
    @DisplayName("Guardar nuevo personaje")
    @Transactional
    @Commit
    @Order(3)
    void saveCharacter() {
        CharacterEntity character = CharacterEntity.builder()
                .name("Nombre")
                .description("Descripción")
                .book(bookRepository.findById(1L).get())
                .build();
        CharacterEntity res = characterRepository.save(character);
        assertThat(res).isNotNull();
        assertThat(res.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar un personaje")
    @Transactional
    @Commit
    @Order(4)
    void updateCharacter() {
        Optional<CharacterEntity> character = characterRepository.findById(1L);
        assertThat(character).isPresent();
        CharacterEntity characterToUpdate = character.get();
        characterToUpdate.setName("Actualizado");
        CharacterEntity updatedCharacter = characterRepository.save(characterToUpdate);
        assertThat(updatedCharacter.getName()).isEqualTo("Actualizado");
    }

    @Test
    @DisplayName("Eliminar un personaje")
    @Transactional
    @Commit
    @Order(5)
    void removeCharacter() {
        characterRepository.deleteById(1L);
        Optional<CharacterEntity> characterVoid = characterRepository.findById(1L);
        assertThat(characterVoid).isEmpty();
    }
}
