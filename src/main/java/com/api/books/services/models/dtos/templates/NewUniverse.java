package com.api.books.services.models.dtos.templates;

import com.api.books.persistence.entities.AuthorEntity;
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
public class NewUniverse {

    @Autowired
    AuthorRepository authorRepository;

    private String name;
    private Long userId;
    private List<AuthorDTO> authors = new ArrayList<>();

    public NewUniverse(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    public List<AuthorEntity> getAuthorEntities() {
        List<AuthorEntity> authorEntities = new ArrayList<>();
        for (AuthorDTO authorDTO : authors)
            authorEntities.add(authorRepository.findById(authorDTO.getAuthorId()).get());
        return authorEntities;
    }
}
