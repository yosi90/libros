package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
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
public class UniverseDTO extends DTOCosmicEntity {

    private Long universeId;
    private String name;
    private Long userId;
    private List<Long> authorIds = new ArrayList<>();
    private List<Long> sagaIds = new ArrayList<>();
    private List<Long> bookIds = new ArrayList<>();

    public UniverseDTO(UniverseEntity universe) {
        universeId = universe.getId();
        name = universe.getName();
        userId = universe.getUserUniverses().getId();
        if(universe.getAuthorsUniverses() != null && !universe.getAuthorsUniverses().isEmpty())
            authorIds = universe.getAuthorsUniverses().stream().map(AuthorEntity::getId).collect(Collectors.toList());
        if(universe.getSagasUniverse() != null && !universe.getSagasUniverse().isEmpty())
            sagaIds = universe.getSagasUniverse().stream().map(SagaEntity::getId).collect(Collectors.toList());
        if(universe.getBooksUniverse() != null && !universe.getBooksUniverse().isEmpty())
            bookIds = universe.getBooksUniverse().stream().map(BookEntity::getId).collect(Collectors.toList());
    }
}
