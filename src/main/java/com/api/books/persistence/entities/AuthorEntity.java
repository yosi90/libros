package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.AuthorDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "author_books",
            joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private List<BookEntity> books_authored;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private UserEntity userAuthors;

    public AuthorDTO toDTO() { return new AuthorDTO(this); }
}
