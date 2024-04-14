package com.api.books.services.models.dtos;

import com.api.books.services.models.entities.DTOCosmicEntity;
import com.api.books.persistence.entities.CharacterEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDTO extends DTOCosmicEntity {

    private Long characterId;
    private String name;
    private String description;

    public CharacterDTO(CharacterEntity character) {
        characterId = character.getId();
        name = character.getName();
        description = character.getDescription();
        String bookUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/book/{bookId}")
                .buildAndExpand(character.getBook().getId())
                .toUriString();
        newURI("Book", bookUri);
        String chaptersUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/character/{characterId}/chapters")
                .buildAndExpand(characterId)
                .toUriString();
        newURI("Chapters", chaptersUri);
    }
}
