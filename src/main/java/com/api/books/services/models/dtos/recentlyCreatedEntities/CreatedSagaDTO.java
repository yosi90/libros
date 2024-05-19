package com.api.books.services.models.dtos.recentlyCreatedEntities;

import com.api.books.persistence.entities.SagaEntity;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.entities.DTOCosmicEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedSagaDTO extends DTOCosmicEntity {

    private Long sagaId;
    private String name;
    private Long userId;
    private Long universeId;
    private List<AuthorDTO> authors = new ArrayList<>();
    private UniverseDTO universe;

    public CreatedSagaDTO(SagaEntity saga) {
        sagaId = saga.getId();
        name = saga.getName();
        userId = saga.getUserSagas().getId();
        if (saga.getAuthorsSagas() != null && !saga.getAuthorsSagas().isEmpty())
            authors = saga.getAuthorsDTOs();
        universeId = saga.getUniverseSagas().getId();
        universe = saga.getUniverseSagas().toDTO();
    }
}