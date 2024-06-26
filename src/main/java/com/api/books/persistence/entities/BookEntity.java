package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import com.api.books.services.models.dtos.ReadStatusDTO;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedBookDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = { "owner", "authorsBooks", "universeBooks", "sagaBooks", "characters", "statusBook" })
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    private String cover;

    private int orderInSaga = -1;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonIgnore
    private UserEntity owner;

    @OneToMany(mappedBy = "readStatusBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReadStatusEntity> statusBook = new ArrayList<>();
    public List<ReadStatusDTO> getStatusDTOs() {
        List<ReadStatusDTO> readStatusDTOs = new ArrayList<>();
        for (ReadStatusEntity status : statusBook)
            readStatusDTOs.add(status.toDTO());
        return readStatusDTOs;
    }

    @ManyToMany(mappedBy = "booksAuthors")
    List<AuthorEntity> authorsBooks;
    public List<AuthorDTO> getAuthorsDTOs() {
        List<AuthorDTO> authorDTOs = new ArrayList<>();
        for (AuthorEntity author : authorsBooks)
            authorDTOs.add(author.toDTO());
        return authorDTOs;
    }

    @ManyToOne
    @JoinColumn(name = "universe_id", referencedColumnName = "id")
    @JsonIgnore
    private UniverseEntity universeBooks;

    @ManyToOne
    @JoinColumn(name = "saga_id", referencedColumnName = "id")
    @JsonIgnore
    private SagaEntity sagaBooks;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference(value = "book_chapters")
    private List<ChapterEntity> chapters = new ArrayList<>();

    public List<ChapterDTO> getChaptersDTOs() {
        List<ChapterDTO> chapterDTOs = new ArrayList<>();
        for (ChapterEntity chapter : chapters)
            chapterDTOs.add(chapter.toDTO());
        return chapterDTOs;
    }

    @OneToMany(mappedBy = "bookCharacters", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference(value = "book_characters")
    private List<CharacterEntity> characters = new ArrayList<>();

    public List<CharacterDTO> getCharactersDTOs() {
        List<CharacterDTO> characterDTOs = new ArrayList<>();
        for (CharacterEntity character : characters)
            characterDTOs.add(character.toDTO());
        return characterDTOs;
    }

    public BookDTO toDTO() {
        return new BookDTO(this);
    }

    public CreatedBookDTO toCDTO() {
        return new CreatedBookDTO(this);
    }
}
