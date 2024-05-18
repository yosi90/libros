package com.api.books.controllers;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.AuthService;
import com.api.books.services.UserService;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.UserRolesDTO;
import com.api.books.services.models.dtos.askers.PasswordUpdateDTO;
import com.api.books.services.models.dtos.askers.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserControllers {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @GetMapping("/{userId}")
    @Operation(summary = "Obtener usuario por ID", description = "Recupera un usuario existente utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El usuario ha sido encontrado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista de todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente.")
    public ResponseEntity<List<UserRolesDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}/roles")
    @Operation(summary = "Obtener roles de usuario", description = "Recupera los roles de un usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los roles del usuario han sido encontrados exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<UserRolesDTO> getUserRoles(@PathVariable Long userId) {
        return userService.getUserRoles(userId);
    }

    @GetMapping("/{userId}/authors")
    @Operation(summary = "Obtener autores de un usuario", description = "Recupera los autores de un usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los autores del usuario han sido encontrados exitosamente."),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado.")
    })
    public ResponseEntity<List<AuthorDTO>> getUserAuthors(@PathVariable Long userId) {
        return userService.getUserAuthors(userId);
    }

    @GetMapping("/{userId}/universes")
    @Operation(summary = "Obtener universos de un usuario", description = "Recupera los universos de un usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los universos del usuario han sido encontrados exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<List<UniverseDTO>> getUserUniverses(@PathVariable Long userId) {
        return userService.getUserUniverses(userId);
    }

    @GetMapping("/{userId}/sagas")
    @Operation(summary = "Obtener sagas de un usuario", description = "Recupera las sagas de un usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las sagas del usuario han sido encontrados exitosamente."),
            @ApiResponse(responseCode = "404", description = "Saga no encontrada.")
    })
    public ResponseEntity<List<SagaDTO>> getUserSagas(@PathVariable Long userId) {
        return userService.getUserSagas(userId);
    }

    @GetMapping("/{userId}/books")
    @Operation(summary = "Obtener libros de un usuario", description = "Recupera los libros asociados a un usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los libros del usuario han sido encontrados exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<List<BookDTO>> getUserBooks(@PathVariable Long userId) {
        return userService.getUserBooks(userId);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El usuario ha sido creado exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    public ResponseEntity<ResponseDTO> createUser(@RequestBody @Valid UserEntity userNew, BindingResult result)
            throws Exception {
        return authService.registerAdmin(userNew, result);
    }

    @PatchMapping("/{userId}/image")
    @Operation(summary = "Actualizar el nombre de un usuario", description = "Actualiza el nombre de un usuario existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El nombre del usuario ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<UserDTO> updateName(@PathVariable Long userId, @RequestParam("image") MultipartFile image) throws IOException {
        return userService.updateImage(image, userId);
    }

    @PatchMapping("/{userId}/name")
    @Operation(summary = "Actualizar el nombre de un usuario", description = "Actualiza el nombre de un usuario existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El nombre del usuario ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<UserDTO> updateName(@PathVariable Long userId, @RequestBody String nameNew) {
        return userService.updateName(userId, nameNew);
    }

    @PatchMapping("/{userId}/email")
    @Operation(summary = "Actualizar el correo electrónico de un usuario", description = "Actualiza el correo electrónico de un usuario existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El correo electrónico del usuario ha sido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<UserDTO> updateEmail(@PathVariable Long userId, @RequestBody String emailNew) {
        return userService.updateEmail(userId, emailNew);
    }

    @PatchMapping("/{userId}/password")
    @Operation(summary = "Actualizar la contraseña de un usuario", description = "Actualiza la contraseña de un usuario existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La contraseña del usuario ha sido actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "406", description = "Error: Contraseña no válida.")
    })
    public ResponseEntity<UserDTO> updatePassword(@PathVariable Long userId,
            @RequestBody PasswordUpdateDTO passwords) {
        return userService.updatePassword(userId, passwords);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El usuario ha sido eliminado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<ResponseDTO> removeUser(@PathVariable Long userId) {
        return userService.removeUser(userId);
    }
}
