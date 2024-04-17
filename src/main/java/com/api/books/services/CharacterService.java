package com.api.books.services;

import com.api.books.persistence.entities.CharacterEntity;
import com.api.books.services.models.dtos.CharacterDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CharacterService {

    ResponseEntity<CharacterDTO> getCharacterById(Long characterId);

    ResponseEntity<List<CharacterDTO>> getAllCharacters();

    ResponseEntity<CharacterDTO> addCharacter(CharacterEntity characterNew);
}
