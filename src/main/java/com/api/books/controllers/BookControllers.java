package com.api.books.controllers;

import com.api.books.services.BookService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.askers.NewBook;
import com.api.books.services.models.dtos.recentlyCreatedEntities.CreatedBookDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookControllers {

    private final BookService bookService;

    public BookControllers(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{bookId}/{userId}")
    @Operation(summary = "Obtener un libro por su ID", description = "Recupera un libro existente utilizando su ID y el ID del usuario propietario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El libro ha sido encontrado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Petición incorrecta: El libro no pertenece al usuario."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId, @PathVariable Long userId) {
        return bookService.getBookById(bookId, userId);
    }

    @GetMapping("/created/{bookId}/{userId}")
    @Operation(summary = "Obtener un libro por su ID", description = "Recupera un libro existente utilizando su ID y el ID del usuario propietario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El libro ha sido encontrado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Petición incorrecta: El libro no pertenece al usuario."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<CreatedBookDTO> getCreatedBookById(@PathVariable Long bookId, @PathVariable Long userId) {
        return bookService.getCreatedBookById(bookId, userId);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los libros", description = "Recupera una lista de todos los libros disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de libros recuperada exitosamente.")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    @Operation(summary = "Añadir un nuevo libro", description = "Añade un nuevo libro utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El libro ha sido añadido exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud."),
            @ApiResponse(responseCode = "409", description = "Conflicto: Ya existe un libro con el mismo título."),
            @ApiResponse(responseCode = "404", description = "Dueño del libro no encontrado.")
    })
    public ResponseEntity<CreatedBookDTO> addBook(
            @RequestParam("name") String name,
            @RequestParam("userId") String userId,
            @RequestParam("orderInSaga") String orderInSaga,
            @RequestParam("authors") String authors,
            @RequestParam("status") String status,
            @RequestParam("universe") String universe,
            @RequestParam("saga") String saga,
            @RequestParam("file") MultipartFile file) throws Exception {
        String[] authorIds;
        if (authors.contains(","))
            authorIds = authors.split(",");
        else
            authorIds = new String[] { authors };

        NewBook bookNew = new NewBook(name, Long.parseLong(userId), Integer.parseInt(orderInSaga), status, authorIds, Long.parseLong(universe), Long.parseLong(saga));
        return bookService.addBook(bookNew, file);
    }

    @PutMapping("/{bookId}")
    @Operation(summary = "Actualiza un libro", description = "Actualiza un libro utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El libro ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud."),
            @ApiResponse(responseCode = "404", description = "Dueño del libro no encontrado.")
    })
    public ResponseEntity<CreatedBookDTO> updateBook(@PathVariable Long bookId,
            @RequestParam("name") String name,
            @RequestParam("userId") String userId,
            @RequestParam("orderInSaga") String orderInSaga,
            @RequestParam("authors") String authors,
            @RequestParam("status") String status,
            @RequestParam("universe") String universe,
            @RequestParam("saga") String saga,
            @RequestParam("file") MultipartFile file) throws Exception {
        String[] authorIds;
        if (authors.contains(","))
            authorIds = authors.split(",");
        else
            authorIds = new String[] { authors };

        NewBook bookNew = new NewBook(name, Long.parseLong(userId), Integer.parseInt(orderInSaga), status, authorIds, Long.parseLong(universe), Long.parseLong(saga));
        return bookService.updateBook(bookNew, bookId, file);
    }

    @PatchMapping("/{bookId}/status/{statusId}")
    @Operation(summary = "Actualizar el estado de lectura de un libro", description = "Actualiza el estado de un libro existente identificado por su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El estado del libro ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<BookDTO> updateStatus(@PathVariable Long bookId, @PathVariable Long statusId) {
        return bookService.updateStatus(bookId, statusId);
    }
}
