package com.api.books.controllers;

import com.api.books.services.BookService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.askers.NewBook;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookControllers {

    @Autowired
    BookService bookService;

    @GetMapping("/{bookId}/{userId}")
    @Operation(summary = "Obtener un libro por su ID",
            description = "Recupera un libro existente utilizando su ID y el ID del usuario propietario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El libro ha sido encontrado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Petición incorrecta: El libro no pertenece al usuario."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId, @PathVariable Long userId) {
        return bookService.getBookById(bookId, userId);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los libros",
            description = "Recupera una lista de todos los libros disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de libros recuperada exitosamente.")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    @Operation(summary = "Añadir un nuevo libro",
            description = "Añade un nuevo libro utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El libro ha sido añadido exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud."),
            @ApiResponse(responseCode = "409", description = "Conflicto: Ya existe un libro con el mismo título."),
            @ApiResponse(responseCode = "404", description = "Dueño del libro no encontrado.")
    })
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid NewBook bookNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new BookDTO(), HttpStatus.NOT_ACCEPTABLE);
        return bookService.addBook(bookNew);
    }

    @PatchMapping("/{bookId}/cover")
    @Operation(summary = "Actualizar la portada de un libro",
            description = "Actualiza la portada de un libro existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La portada del libro ha sido actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<BookDTO> updateCover(@PathVariable Long bookId, @RequestParam("cover") MultipartFile file) {
        return bookService.updateCover(bookId, file);
    }
}
