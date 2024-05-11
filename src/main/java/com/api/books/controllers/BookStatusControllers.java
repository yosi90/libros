package com.api.books.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.books.services.BookStatusService;
import com.api.books.services.models.dtos.BookstatusDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/bookstatus")
public class BookStatusControllers {

    private final BookStatusService bookStatusService;

    public BookStatusControllers(BookStatusService bookStatusService) {
        this.bookStatusService = bookStatusService;
    }

    @GetMapping("/{statusId}")
    @Operation(summary = "Obtener un estado de libro por su ID", description = "Recupera un estado de libro existente utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El estado ha sido encontrado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<BookstatusDTO> getStatusById(@PathVariable Long statusId) {
        return bookStatusService.getStatusById(statusId);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los estados de libros", description = "Recupera una lista de todos los estados de libros.")
    @ApiResponse(responseCode = "200", description = "Lista de estados de libros recuperada exitosamente.")
    public ResponseEntity<List<BookstatusDTO>> getAllStatuses() {
        return bookStatusService.getAllStatuses();
    }
}
