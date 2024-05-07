package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {

    private Long authorId;
    private String name;
    private Long userId;
    private List<Long> universeIds = new ArrayList<>();
    private List<Long> sagaIds = new ArrayList<>();
    private List<Long> bookIds = new ArrayList<>();

    public AuthorDTO(AuthorEntity author) {
        authorId = author.getId();
        name = author.getName();
        userId = author.getUserAuthors().getId();
        if(author.getUniversesAuthors() != null && !author.getUniversesAuthors().isEmpty())
            universeIds = author.getUniversesAuthors().stream().map(UniverseEntity::getId).collect(Collectors.toList());
        if(author.getSagasAuthors() != null && !author.getSagasAuthors().isEmpty())
            sagaIds = author.getSagasAuthors().stream().map(SagaEntity::getId).collect(Collectors.toList());
        if(author.getBooksAuthors() != null && !author.getBooksAuthors().isEmpty())
            bookIds = author.getBooksAuthors().stream().map(BookEntity::getId).collect(Collectors.toList());
    }
}
