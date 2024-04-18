package com.api.books.services;

import com.api.books.persistence.entities.CharacterEntity;
import com.api.books.services.models.dtos.CharacterDTO;
import com.api.books.services.models.dtos.templates.NewCharacter;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CharacterService {

    ResponseEntity<CharacterDTO> getCharacterById(Long characterId);

    ResponseEntity<List<CharacterDTO>> getAllCharacters();

    ResponseEntity<CharacterDTO> addCharacter(NewCharacter characterNew);

    ResponseEntity<CharacterDTO> updateCharacter(Long id, NewCharacter updatedCharacter);
}
