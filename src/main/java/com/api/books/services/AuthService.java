package com.api.books.services;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.models.dtos.templates.JwtTokenDTO;
import com.api.books.services.models.dtos.templates.LoginDTO;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JwtTokenDTO> login(LoginDTO login) throws Exception;

    ResponseEntity<ResponseDTO> register(UserEntity user) throws Exception;

    ResponseEntity<ResponseDTO> registerAdmin(UserEntity user) throws Exception;
}
