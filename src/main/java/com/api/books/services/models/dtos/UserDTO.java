package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.services.models.entities.DTOCosmicEntity;
import com.api.books.persistence.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends DTOCosmicEntity {

    private Long userId;
    private String name;
    private String email;
    private List<BookDTO> books;
    private List<AuthorDTO> authors;

    public UserDTO(UserEntity user) {
        userId = user.getId();
        name = user.getName();
        email = user.getEmail();
        books = user.getBooksDTOs();
        authors = user.getAuthorsDTOs();
        String booksUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/user/{userId}/books")
                .buildAndExpand(userId)
                .toUriString();
        newURI("Books", booksUri);
        String authorsUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/user/{userId}/authors")
                .buildAndExpand(userId)
                .toUriString();
        newURI("Authors", booksUri);
    }
}
