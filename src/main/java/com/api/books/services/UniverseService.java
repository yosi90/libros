package com.api.books.services;

import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.askers.NewUniverse;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedUniverseDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UniverseService {

    ResponseEntity<CreatedUniverseDTO> getCreatedUniverseById(Long universeId, Long userId);
    
    ResponseEntity<List<UniverseDTO>> getAllUniverses();

    ResponseEntity<CreatedUniverseDTO> addUniverse(NewUniverse universeNew, BindingResult result);

    UniverseEntity addUniverse(NewUniverse universeNew);

    ResponseEntity<CreatedUniverseDTO> updateUniverse(NewUniverse universeNew, Long universeId);

    ResponseEntity<UniverseDTO> updateName(Long id, String nameNew);
}
