package com.api.books.services.models.dtos.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBook {

    private String title;
    private String author;
    private Long ownerId;
}
