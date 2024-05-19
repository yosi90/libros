package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedSagaDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = { "userSagas", "authorsSagas", "universeSagas", "booksSagas" })
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sagas")
public class SagaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private UserEntity userSagas;

    @ManyToMany(mappedBy = "sagasAuthors")
    List<AuthorEntity> authorsSagas;
    public List<AuthorDTO> getAuthorsDTOs() {
        List<AuthorDTO> authorDTOs = new ArrayList<>();
        for (AuthorEntity author : authorsSagas)
            authorDTOs.add(author.toDTO());
        return authorDTOs;
    }

    @ManyToOne
    @JoinColumn(name = "universe_id", referencedColumnName = "id")
    @JsonIgnore
    private UniverseEntity universeSagas;

    @JsonBackReference(value = "user_books")
    @OneToMany(mappedBy = "sagaBooks", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookEntity> booksSagas;
    public List<BookDTO> getBooksDTOs() {
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (BookEntity book : booksSagas)
            bookDTOS.add(book.toDTO());
        return bookDTOS;
    }

    public SagaEntity(String name) {
        this.name = name;
    }

    public SagaDTO toDTO() { return new SagaDTO(this); }

    public CreatedSagaDTO toCDTO() { return new CreatedSagaDTO(this); }
}
