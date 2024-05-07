package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.UniverseDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "universes")
public class UniverseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private UserEntity userUniverses;

    @ManyToMany(mappedBy = "universesAuthors")
    List<AuthorEntity> authorsUniverses;

    @JsonBackReference(value = "universe_sagas")
    @OneToMany(mappedBy = "universeSagas", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SagaEntity> sagasUniverse;
    public List<SagaDTO> getSagasDTOs() {
        List<SagaDTO> sagaDTOs = new ArrayList<>();
        for (SagaEntity saga : sagasUniverse)
            sagaDTOs.add(saga.toDTO());
        return sagaDTOs;
    }

    public void addSaga(SagaEntity saga) {
        if(sagasUniverse == null)
            sagasUniverse = new ArrayList<>();
        sagasUniverse.add(saga);
    }

    @JsonBackReference(value = "universe_books")
    @OneToMany(mappedBy = "universeBooks", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookEntity> booksUniverse;
    public List<BookDTO> getBooksDTOs() {
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (BookEntity book : booksUniverse)
            bookDTOS.add(book.toDTO());
        return bookDTOS;
    }

    public UniverseDTO toDTO() { return new UniverseDTO(this); }
}