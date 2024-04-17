package com.api.books.controllers;

import com.api.books.persistence.entities.CharacterEntity;
import com.api.books.services.BookService;
import com.api.books.services.CharacterService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import com.api.books.services.models.dtos.templates.NewBook;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/character")
public class Charactercontroller {
    @Autowired
    CharacterService characterService;

    @GetMapping("/{characterId}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable Long characterId) {
        return characterService.getCharacterById(characterId);
    }

    @GetMapping
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @PostMapping
    public ResponseEntity<CharacterDTO> addCharacter(@RequestBody @Valid CharacterEntity characterNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new CharacterDTO(), HttpStatus.NOT_ACCEPTABLE);
        return characterService.addCharacter(characterNew);
    }
}
