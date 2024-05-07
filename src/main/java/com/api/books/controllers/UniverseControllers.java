package com.api.books.controllers;

import com.api.books.services.UniverseService;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.templates.NewUniverse;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/universe")
public class UniverseControllers {

    @Autowired
    private UniverseService universeService;

    @PostMapping
    @Operation(summary = "Crear un nuevo autor",
            description = "Crea un nuevo autor utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El autor ha sido creado exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    public ResponseEntity<ResponseDTO> addUniverse(@RequestBody @Valid NewUniverse universeNew, BindingResult result) {
        return universeService.addUniverse(universeNew, result);
    }

    @PatchMapping("/{universeId}/name")
    @Operation(summary = "Actualizar el nombre de un autor",
            description = "Actualiza el nombre de un autor existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El nombre del autor ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado.")
    })
    public ResponseEntity<UniverseDTO> updateName(@PathVariable Long universeId, @RequestBody String nameNew) {
        return universeService.updateName(universeId, nameNew);
    }
}
