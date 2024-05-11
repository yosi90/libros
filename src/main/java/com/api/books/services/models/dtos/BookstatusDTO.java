package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.BookStatusEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookstatusDTO {
    
    private Long statusId;
    private String name;

    public BookstatusDTO(BookStatusEntity bookStatus) {
        statusId = bookStatus.getId();
        name = bookStatus.getName();
    }
}
