package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    private Boolean isRead = false;

    @NotBlank
    @Size(min = 3, max = 30)
    private String author;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChapterEntity> chapters;

    public List<ChapterDTO> getChaptersDTOs() {
        List<ChapterDTO> chapterDTOs = new ArrayList<>();
        for (ChapterEntity chapter : chapters)
            chapterDTOs.add(chapter.ToDTO());
        return chapterDTOs;
    }

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CharacterEntity> characters;

    public List<CharacterDTO> getCharactersDTOs() {
        List<CharacterDTO> characterDTOs = new ArrayList<>();
        for (CharacterEntity character : characters)
            characterDTOs.add(character.ToDTO());
        return characterDTOs;
    }

    public BookDTO ToDTO() {
        return new BookDTO(this);
    }
}
