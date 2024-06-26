package com.api.books.controllers;

import com.api.books.services.AuthorService;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.askers.NewAuthor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorControllers {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/{authorId}")
    @Operation(summary = "Obtener autor por ID", description = "Recupera un autor existente utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El autor ha sido encontrado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado.")
    })
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long authorId) {
        return authorService.getAuthorById(authorId);
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo autor",
            description = "Crea un nuevo autor utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El autor ha sido creado exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody @Valid NewAuthor authorNew, BindingResult result) {
        return authorService.addAuthor(authorNew, result);
    }

    @PatchMapping("/{authorId}/name")
    @Operation(summary = "Actualizar el nombre de un autor",
            description = "Actualiza el nombre de un autor existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El nombre del autor ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado.")
    })
    public ResponseEntity<AuthorDTO> updateName(@PathVariable Long authorId, @RequestBody String nameNew) {
        return authorService.updateName(authorId, nameNew);
    }
}
