package com.api.books.services.models.dtos;

import java.time.LocalDateTime;

import com.api.books.persistence.entities.ReadStatusEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadStatusDTO {

    private Long readStatusId;
    private LocalDateTime date;
    private Long bookId;
    private BookstatusDTO status;

    public ReadStatusDTO(ReadStatusEntity readStatusEntity){
        readStatusId = readStatusEntity.getId();
        date = readStatusEntity.getDate();
        bookId = readStatusEntity.getReadStatusBook().getId();
        status = readStatusEntity.getReadStatus().toDTO();
    }
}
