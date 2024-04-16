package com.api.books.services.models.dtos;

import com.api.books.services.models.entities.DTOCosmicEntity;
import com.api.books.persistence.entities.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO extends DTOCosmicEntity {

    private Long bookId;
    private String name;
    private String author;
    private String cover;
    private boolean read;
    private Long ownerId;
    private List<ChapterDTO> chapters;
    private List<CharacterDTO> characters;

    public BookDTO(BookEntity book) {
        bookId = book.getId();
        name = book.getName();
        author = book.getAuthor();
        cover = book.getCover();
        read = book.getIsRead();
        ownerId = book.getOwner().getId();
        chapters = book.getChaptersDTOs();
        characters = book.getCharactersDTOs();
        String chaptersUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/book/{bookId}/chapters")
                .buildAndExpand(bookId)
                .toUriString();
        newURI("Chapters", chaptersUri);
        String charactersUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/book/{bookId}/characters")
                .buildAndExpand(bookId)
                .toUriString();
        newURI("Characters", charactersUri);
    }
}
