package com.api.books.services.models.dtos.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewChapter {
    private String name;
    private String  description;
    private Long bookId;
    private Integer orderInBook;
    private List<Long> charactersId;
}
