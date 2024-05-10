package com.api.books.services.models.dtos.askers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAuthor {

    private String name;
    private Long userId;
}
