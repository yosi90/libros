package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long bookId;
    private String name;
    private int orderInSaga;
    private String cover;
    private Long userId;
    private Long universeId;
    private Long sagaId;
    private List<ReadStatusDTO> status;
    private List<AuthorDTO> authors = new ArrayList<>();
    private List<ChapterDTO> chapters = new ArrayList<>();
    private List<CharacterDTO> characters = new ArrayList<>();

    public BookDTO(BookEntity book) {
        bookId = book.getId();
        name = book.getName();
        orderInSaga = book.getOrderInSaga();
        cover = book.getCover();
        status = book.getStatusDTOs();
        userId = book.getOwner().getId();
        universeId = book.getUniverseBooks().getId();
        sagaId = book.getSagaBooks().getId();
        if(!book.getAuthorsBooks().isEmpty())
            authors = book.getAuthorsDTOs();
        chapters = book.getChaptersDTOs();
        characters = book.getCharactersDTOs();
    }
}
