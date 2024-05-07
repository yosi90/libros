package com.api.books.services.models.dtos;

import com.api.books.services.models.entities.DTOCosmicEntity;
import com.api.books.persistence.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends DTOCosmicEntity {

    private Long userId;
    private String name;
    private String email;
    private List<AuthorDTO> authors = new ArrayList<>();
    private List<UniverseDTO> universes = new ArrayList<>();
    private List<SagaDTO> sagas = new ArrayList<>();
    private List<BookDTO> books = new ArrayList<>();

    public UserDTO(UserEntity user) {
        userId = user.getId();
        name = user.getName();
        email = user.getEmail();
        if(user.getAuthors() != null && !user.getAuthors().isEmpty())
            authors = user.getAuthorsDTOs();
        if(user.getUniverses() != null && !user.getUniverses().isEmpty())
            universes = user.getUniversesDTOs();
        if(user.getSagas() != null && !user.getSagas().isEmpty())
            sagas = user.getSagasDTOs();
        if(user.getBooks() != null && !user.getBooks().isEmpty())
            books = user.getBooksDTOs();
    }
}
