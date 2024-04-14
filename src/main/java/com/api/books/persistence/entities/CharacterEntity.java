package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "characters")
public class CharacterEntity extends CosmicEntity {

    private String description;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "apariciones",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "chapter_id")
    )
    private Set<ChapterEntity> chapters;

    public List<ChapterDTO> getChaptersDTOs() {
        List<ChapterDTO> chapterDTOs = new ArrayList<>();
        for (ChapterEntity chapter : chapters)
            chapterDTOs.add(chapter.ToDTO());
        return chapterDTOs;
    }

    public CharacterDTO ToDTO() {
        return new CharacterDTO(this);
    }
}
