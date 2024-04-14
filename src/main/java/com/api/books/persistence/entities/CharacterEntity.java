package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "characters")
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;

    public CharacterDTO ToDTO() {
        return new CharacterDTO(this);
    }
}
