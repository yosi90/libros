package com.api.books.persistence.entities;

import java.time.LocalDateTime;

import com.api.books.services.models.dtos.ReadStatusDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = { "readStatusBook", "readStatus" })
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_read_status")
public class ReadStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity readStatusBook;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private BookStatusEntity readStatus;

    public ReadStatusDTO toDTO(){
        return new ReadStatusDTO(this);
    }
}