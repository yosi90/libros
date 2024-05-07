package com.api.books.services;

import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.templates.NewUniverse;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UniverseService {

    ResponseEntity<ResponseDTO> addUniverse(NewUniverse universeNew, BindingResult result);

    UniverseEntity addUniverse(NewUniverse universeNew);

    ResponseEntity<UniverseDTO> updateName(Long id, String nameNew);
}
