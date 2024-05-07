package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.services.models.entities.DTOCosmicEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SagaDTO extends DTOCosmicEntity {

    private Long sagaId;
    private String name;
    private Long userId;
    private List<Long> authorIds = new ArrayList<>();
    private Long universeId;
    private List<Long> bookIds = new ArrayList<>();

    public SagaDTO(SagaEntity saga) {
        sagaId = saga.getId();
        name = saga.getName();
        userId = saga.getUserSagas().getId();
        if (saga.getAuthorsSagas() != null && !saga.getAuthorsSagas().isEmpty())
            authorIds = saga.getAuthorsSagas().stream().map(AuthorEntity::getId).collect(Collectors.toList());
        if (sagaId > 1)
            universeId = saga.getUniverseSagas().getId();
        if (saga.getBooksSagas() != null && !saga.getBooksSagas().isEmpty())
            bookIds = saga.getBooksSagas().stream().map(BookEntity::getId).collect(Collectors.toList());
    }
}
