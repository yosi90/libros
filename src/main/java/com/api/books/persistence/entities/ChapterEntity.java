package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
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

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    private BookEntity origin;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "chapter_characters",
            joinColumns = @JoinColumn(name = "chapter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id")
    )
    private Set<CharacterEntity> characters;

    public List<CharacterDTO> getCharactersDTOs() {
        List<CharacterDTO> charactersDTOs = new ArrayList<>();
        for (CharacterEntity character : characters)
            charactersDTOs.add(character.ToDTO());
        return charactersDTOs;
    }

    public ChapterDTO ToDTO() {
        return new ChapterDTO(this);
    }
}
