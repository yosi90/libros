package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "chapters")
public class ChapterEntity extends CosmicEntity {

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity origin;

    @ManyToMany
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
