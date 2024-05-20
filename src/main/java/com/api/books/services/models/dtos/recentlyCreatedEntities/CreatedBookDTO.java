package com.api.books.services.models.dtos.recentlyCreatedEntities;

import java.util.ArrayList;
import java.util.List;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.BookstatusDTO;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.UniverseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedBookDTO {

    private Long bookId;
    private String name;
    private int orderInSaga;
    private String cover;
    private Long userId;
    private Long universeId;
    private UniverseDTO universe;
    private Long sagaId;
    private SagaDTO saga;
    private BookstatusDTO status;
    private List<AuthorDTO> authors = new ArrayList<>();

    public CreatedBookDTO(BookEntity book) {
        bookId = book.getId();
        name = book.getName();
        orderInSaga = book.getOrderInSaga();
        cover = book.getCover();
        status = book.getStatusBooks().toDTO();
        userId = book.getOwner().getId();
        universe = book.getUniverseBooks().toDTO();
        saga = book.getSagaBooks().toDTO();
        if(!book.getAuthorsBooks().isEmpty())
            authors = book.getAuthorsDTOs();
    }
}