package com.api.books.services;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.models.dtos.askers.JwtTokenDTO;
import com.api.books.services.models.dtos.askers.LoginDTO;
import com.api.books.services.models.dtos.askers.ResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface AuthService {
    ResponseEntity<JwtTokenDTO> login(LoginDTO login) throws Exception;

    ResponseEntity<ResponseDTO> register(UserEntity user, BindingResult result) throws Exception;

    ResponseEntity<ResponseDTO> registerAdmin(UserEntity user, BindingResult result) throws Exception;
}
