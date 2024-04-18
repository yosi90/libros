package com.api.books.controllers;

import com.api.books.services.ChapterService;
import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.templates.NewChapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/chapter")
public class ChapterControllers {

    @Autowired
    ChapterService chapterService;

    @Operation(summary = "Añadir un nuevo capítulo",
            description = "Añade un nuevo capítulo al libro con la información proporcionada en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El capítulo ha sido añadido exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud."),
            @ApiResponse(responseCode = "409", description = "Conflicto: Ya existe un capítulo con el mismo nombre."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    @PostMapping
    public ResponseEntity<ChapterDTO> addChapter(@RequestBody @Valid NewChapter chapterNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new ChapterDTO(), HttpStatus.NOT_ACCEPTABLE);
        return chapterService.addChapter(chapterNew);
    }

    @Operation(summary = "Actualizar un capítulo existente",
            description = "Actualiza la información de un capítulo existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El capítulo ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Capítulo no encontrado."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    @PutMapping("/{chapterId}")
    public ResponseEntity<ChapterDTO> updateCharacter(@PathVariable Long chapterId, @RequestBody @Valid NewChapter chapterNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new ChapterDTO(), HttpStatus.NOT_ACCEPTABLE);
        return chapterService.updateChapter(chapterId, chapterNew);
    }
}
