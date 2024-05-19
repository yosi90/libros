package com.api.books.services.models.dtos.recentlyCreatedEntities;

import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.entities.DTOCosmicEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedUniverseDTO extends DTOCosmicEntity {

    private Long universeId;
    private String name;
    private Long userId;
    private List<AuthorDTO> authors = new ArrayList<>();
    private List<SagaDTO> sagas = new ArrayList<>();

    public CreatedUniverseDTO(UniverseEntity universe) {
        universeId = universe.getId();
        name = universe.getName();
        userId = universe.getUserUniverses().getId();
        if(universe.getAuthorsUniverses() != null && !universe.getAuthorsUniverses().isEmpty())
            authors = universe.getAuthorsDTOs();
        if(universe.getSagasUniverse() != null && !universe.getSagasUniverse().isEmpty())
            sagas = universe.getSagasDTOs();
    }
}
