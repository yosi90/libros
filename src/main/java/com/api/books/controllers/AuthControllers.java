package com.api.books.controllers;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.AuthService;
import com.api.books.services.UserService;
import com.api.books.services.models.dtos.templates.JwtTokenDTO;
import com.api.books.services.models.dtos.templates.LoginDTO;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllers {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Registro de usuario",
            description = "Permite registrar un nuevo usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    private ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserEntity userNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors()) {
            ResponseDTO response = new ResponseDTO();
            for (FieldError error : result.getFieldErrors())
                response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        return authService.register(userNew);
    }

    @PostMapping("/registeradmin")
    @Operation(summary = "Registro de administrador",
            description = "Permite registrar un nuevo usuario con rol de administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario administrador registrado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autorizado para realizar esta acción."),
            @ApiResponse(responseCode = "406", description = "Error de validación en el cuerpo de la solicitud.")
    })
    private ResponseEntity<ResponseDTO> registerAdmin(@RequestBody @Valid UserEntity userNew, BindingResult result) throws Exception {
        if(!userService.isADMIN())
            return new ResponseEntity<>(new ResponseDTO("No tienes permiso"), HttpStatus.UNAUTHORIZED);
        if (result != null && result.hasErrors()) {
            ResponseDTO response = new ResponseDTO();
            for (FieldError error : result.getFieldErrors())
                response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        return authService.registerAdmin(userNew);
    }

    @PostMapping("/login")
    @Operation(summary = "Inicio de sesión",
            description = "Permite a un usuario iniciar sesión.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    private ResponseEntity<JwtTokenDTO> login(@RequestBody LoginDTO loginRequest) throws Exception {
        return authService.login(loginRequest);
    }
}
