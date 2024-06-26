package com.api.books.services.models.dtos;

import com.api.books.services.models.entities.DTOCosmicEntity;
import com.api.books.persistence.entities.ChapterEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDTO extends DTOCosmicEntity {

    private Long chapterId;
    private String name;
    private String description;
    private Integer orderInBook;
    private Long bookId;
    private List<CharacterDTO> characters;

    public ChapterDTO(ChapterEntity chapter) {
        chapterId = chapter.getId();
        name = chapter.getName();
        description = chapter.getDescription();
        orderInBook = chapter.getOrderInBook();
        bookId = chapter.getOrigin().getId();
        characters = chapter.getCharactersDTOs();
        String bookUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/book/{bookId}")
                .buildAndExpand(chapter.getOrigin().getId())
                .toUriString();
        newURI("Book", bookUri);
        String charactersUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/chapter/{chapterId}/characters")
                .buildAndExpand(chapterId)
                .toUriString();
        newURI("Characters", charactersUri);
    }
}
