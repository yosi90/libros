package com.api.books.services.models.dtos.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSaga {

    @JsonProperty("name")
    private String name;
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("author")
    private String author;
    @JsonProperty("universe")
    private String universe;
}
