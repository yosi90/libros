package com.api.books.services.models.dtos.templates;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.services.models.dtos.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSaga {

    @Autowired
    AuthorRepository authorRepository;

    private String name;
    private Long userId;
    private UniverseEntity universe;
    private List<AuthorDTO> authors = new ArrayList<>();

    public NewSaga(String name, UniverseEntity universe) {
        this.name = name;
        this.userId = universe.getUserUniverses().getId();
        this.universe = universe;
    }

    public List<AuthorEntity> getAuthorEntities() {
        List<AuthorEntity> authorEntities = new ArrayList<>();
        for (AuthorDTO authorDTO : authors)
            authorEntities.add(authorRepository.findById(authorDTO.getAuthorId()).get());
        return authorEntities;
    }
}