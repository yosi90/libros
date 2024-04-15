package com.api.books.controllers;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.AuthService;
import com.api.books.services.models.dtos.JwtTokenDTO;
import com.api.books.services.models.dtos.LoginDTO;
import com.api.books.services.models.dtos.ResponseDTO;
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

    @PostMapping("/register")
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
    private ResponseEntity<ResponseDTO> registerAdmin(@RequestBody @Valid UserEntity userNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors()) {
            ResponseDTO response = new ResponseDTO();
            for (FieldError error : result.getFieldErrors())
                response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        return authService.registerAdmin(userNew);
    }

    @PostMapping("/login")
    private ResponseEntity<JwtTokenDTO> login(@RequestBody LoginDTO loginRequest) throws Exception {
        return authService.login(loginRequest);
    }
}
