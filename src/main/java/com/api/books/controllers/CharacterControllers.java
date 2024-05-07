package com.api.books.controllers;

import com.api.books.services.CharacterService;
import com.api.books.services.models.dtos.CharacterDTO;
import com.api.books.services.models.dtos.templates.NewCharacter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/character")
public class CharacterControllers {

    @Autowired
    CharacterService characterService;


    @GetMapping("/{characterId}")
    @Operation(summary = "Obtener un personaje por su ID",
            description = "Recupera un personaje existente utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El personaje ha sido encontrado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Personaje no encontrado.")
    })
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable Long characterId) {
        return characterService.getCharacterById(characterId);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los personajes",
            description = "Recupera una lista de todos los personajes disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de personajes recuperada exitosamente.")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @PostMapping
    @Operation(summary = "Añadir un nuevo personaje",
            description = "Añade un nuevo personaje utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El personaje ha sido añadido exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud."),
            @ApiResponse(responseCode = "409", description = "Conflicto: Ya existe un personaje con el mismo nombre."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<CharacterDTO> addCharacter(@RequestBody @Valid NewCharacter characterNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new CharacterDTO(), HttpStatus.NOT_ACCEPTABLE);
        return characterService.addCharacter(characterNew);
    }

    @PutMapping("/{characterId}")
    @Operation(summary = "Actualizar un personaje existente",
            description = "Actualiza la información de un personaje existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El personaje ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Personaje no encontrado."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable Long characterId, @RequestBody @Valid NewCharacter characterNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new CharacterDTO(), HttpStatus.NOT_ACCEPTABLE);
        return characterService.updateCharacter(characterId, characterNew);
    }
}
