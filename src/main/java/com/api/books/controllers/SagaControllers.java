package com.api.books.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.books.services.SagaService;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.askers.NewSaga;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedSagaDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/saga")
public class SagaControllers {

    private final SagaService sagaService;

    public SagaControllers(SagaService sagaService) {
        this.sagaService = sagaService;
    }

    @GetMapping
    public ResponseEntity<List<SagaDTO>> getAllSagas() {
        return sagaService.getAllSagas();
    }

    @GetMapping("/created/{sagaId}/{userId}")
    @Operation(summary = "Obtener una saga por su ID", description = "Recupera una saga existente utilizando su ID y el ID del usuario propietario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La saga ha sido encontrada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Petición incorrecta: La saga no pertenece al usuario."),
            @ApiResponse(responseCode = "404", description = "Saga no encontradq.")
    })
    public ResponseEntity<CreatedSagaDTO> getCreatedUniverseById(@PathVariable Long sagaId, @PathVariable Long userId) {
        return sagaService.getCreatedSagaById(sagaId, userId);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva saga", description = "Crea una nueva saga utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La saga ha sido creado exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    public ResponseEntity<CreatedSagaDTO> addSaga(@RequestBody NewSaga sagaNew) {
        return sagaService.addSaga(sagaNew);
    }

    @PatchMapping("/{sagaId}/name")
    @Operation(summary = "Actualizar el nombre de una saga", description = "Actualiza el nombre de una saga existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El nombre de la saga ha sido actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Saga no encontrada.")
    })
    public ResponseEntity<SagaDTO> updateName(@PathVariable Long sagaId, @RequestBody String nameNew) {
        return sagaService.updateName(sagaId, nameNew);
    }
}
