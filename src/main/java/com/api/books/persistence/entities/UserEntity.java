package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    LocalDateTime lifeSpan = LocalDateTime.now();

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9]"
            + "(?:[a-zA-Z0-9-]*[a-zA-Z0-9])?", message = "Email no válido")
    private String email;

    private String password;

    @JsonBackReference(value = "user_authors")
    @OneToMany(mappedBy = "userAuthors", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AuthorEntity> authors;
    public List<AuthorDTO> getAuthorsDTOs() {
        List<AuthorDTO> authorDTOs = new ArrayList<>();
        for (AuthorEntity author : authors)
            authorDTOs.add(author.toDTO());
        return authorDTOs;
    }

    @JsonBackReference(value = "user_universes")
    @OneToMany(mappedBy = "userUniverses", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UniverseEntity> universes;
    public List<UniverseDTO> getUniversesDTOs() {
        List<UniverseDTO> universeDTOs = new ArrayList<>();
        for (UniverseEntity universe : universes)
            universeDTOs.add(universe.toDTO());
        return universeDTOs;
    }

    public void addUniverse(UniverseEntity universe) {
        if(universes == null)
            universes = new ArrayList<>();
        universes.add(universe);
    }

    @JsonBackReference(value = "user_sagas")
    @OneToMany(mappedBy = "userSagas", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SagaEntity> sagas;
    public List<SagaDTO> getSagasDTOs() {
        List<SagaDTO> sagaDTOs = new ArrayList<>();
        for (SagaEntity saga : sagas)
            sagaDTOs.add(saga.toDTO());
        return sagaDTOs;
    }

    @JsonBackReference(value = "user_books")
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookEntity> books;
    public List<BookDTO> getBooksDTOs() {
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (BookEntity book : books)
            bookDTOS.add(book.toDTO());
        return bookDTOS;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<RoleEntity> roles;

    public UserDTO toDTO() {
        return new UserDTO(this);
    }

    public UserRolesDTO toRolesDTO() { return new UserRolesDTO(this); }
}
