package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = { "origin", "charactersChapters" })
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chapters")
public class ChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @Min(0)
    @Max(100)
    private Integer orderInBook;

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    @JsonIgnore
    private BookEntity origin;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "chapter_characters",
            joinColumns = @JoinColumn(name = "chapter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id")
    )
    @JsonBackReference(value = "chapter_characters")
    private List<CharacterEntity> charactersChapters = new ArrayList<>();
    public List<CharacterDTO> getCharactersDTOs() {
        List<CharacterDTO> charactersDTOs = new ArrayList<>();
        for (CharacterEntity character : charactersChapters)
            charactersDTOs.add(character.toDTO());
        return charactersDTOs;
    }

    public ChapterDTO toDTO() {
        return new ChapterDTO(this);
    }
}
