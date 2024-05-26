package com.api.books.services.models.dtos.askers;

import com.api.books.services.models.dtos.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUniverse {

    private String name;
    private Long userId;
    private List<AuthorDTO> authors = new ArrayList<>();

    public NewUniverse(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }
}
