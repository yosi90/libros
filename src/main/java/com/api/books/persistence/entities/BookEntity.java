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

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity extends CosmicEntity {

    private Boolean read = false;

    @NotBlank
    @Size(min = 3, max = 30)
    private String author;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChapterEntity> chapters;

    public List<ChapterDTO> getChaptersDTOs() {
        List<ChapterDTO> chapterDTOs = new ArrayList<>();
        for (ChapterEntity chapter : chapters)
            chapterDTOs.add(chapter.ToDTO());
        return chapterDTOs;
    }

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
