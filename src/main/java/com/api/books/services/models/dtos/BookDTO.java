package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.services.models.entities.DTOCosmicEntity;
import com.api.books.persistence.entities.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO extends DTOCosmicEntity {

    private Long bookId;
    private String name;
    private String cover;
    private boolean read;
    private Long ownerId;
    private Long universeId;
    private Long sagaId;
    private List<Long> authorIds = new ArrayList<>();
    private List<ChapterDTO> chapters = new ArrayList<>();
    private List<CharacterDTO> characters = new ArrayList<>();

    public BookDTO(BookEntity book) {
        bookId = book.getId();
        name = book.getName();
        cover = book.getCover();
        read = book.getIsRead();
        ownerId = book.getOwner().getId();
        universeId = book.getUniverseBooks().getId();
        sagaId = book.getSagaBooks().getId();
        if(!book.getAuthorsBooks().isEmpty())
            authorIds = book.getAuthorsBooks().stream().map(AuthorEntity::getId).collect(Collectors.toList());
        chapters = book.getChaptersDTOs();
        characters = book.getCharactersDTOs();
    }
}
