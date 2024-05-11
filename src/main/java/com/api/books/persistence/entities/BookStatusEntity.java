package com.api.books.persistence.entities;

import java.util.List;

import com.api.books.services.models.dtos.BookstatusDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = { "booksStatus" })
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "read_status")
public class BookStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "statusBooks", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookEntity> booksStatus;

    public BookStatusEntity(String name) {
        this.name = name;
    }

    public BookstatusDTO toDTO() {
        return new BookstatusDTO(this);
    }
}
