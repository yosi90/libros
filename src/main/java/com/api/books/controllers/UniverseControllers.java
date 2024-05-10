package com.api.books.controllers;

import com.api.books.services.UniverseService;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.templates.NewUniverse;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/universe")
public class UniverseControllers {

    private final UniverseService universeService;

    public UniverseControllers(UniverseService universeService) {
        this.universeService = universeService;
    }

    @GetMapping
    public ResponseEntity<List<UniverseDTO>> getAllUniverses() {
        return universeService.getAllUniverses();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo universo",
            description = "Crea un nuevo universo utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El universo ha sido creado exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    public ResponseEntity<ResponseDTO> addUniverse(@RequestBody @Valid NewUniverse universeNew, BindingResult result) {
        return universeService.addUniverse(universeNew, result);
    }

    @PatchMapping("/{universeId}/name")
    @Operation(summary = "Actualizar el nombre de un universo",
            description = "Actualiza el nombre de un universo existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El nombre del universo ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Universo no encontrado.")
    })
    public ResponseEntity<UniverseDTO> updateName(@PathVariable Long universeId, @RequestBody String nameNew) {
        return universeService.updateName(universeId, nameNew);
    }
}
