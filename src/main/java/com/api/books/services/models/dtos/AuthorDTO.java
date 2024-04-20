package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.AuthorEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {

    private Long authorId;
    private String name;
    private Long userId;

    public AuthorDTO(AuthorEntity author) {
        this.authorId = author.getId();
        this.name = author.getName();
        this.userId = author.getUserAuthors().getId();
    }
}
