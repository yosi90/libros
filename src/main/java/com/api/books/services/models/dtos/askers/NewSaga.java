package com.api.books.services.models.dtos.askers;

import java.util.ArrayList;
import java.util.List;

import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.UniverseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSaga {

    private String name;
    private Long userId;
    private List<AuthorDTO> authors = new ArrayList<>();
    private UniverseDTO universe;
}
