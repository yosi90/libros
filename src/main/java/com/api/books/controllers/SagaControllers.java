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
