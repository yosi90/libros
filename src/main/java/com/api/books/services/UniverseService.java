package com.api.books.services;

import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.askers.NewUniverse;
import com.api.books.services.models.dtos.askers.ResponseDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UniverseService {
    
    ResponseEntity<List<UniverseDTO>> getAllUniverses();

    ResponseEntity<ResponseDTO> addUniverse(NewUniverse universeNew, BindingResult result);

    UniverseEntity addUniverse(NewUniverse universeNew);

    ResponseEntity<UniverseDTO> updateName(Long id, String nameNew);
}
