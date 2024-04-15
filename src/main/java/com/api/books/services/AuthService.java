package com.api.books.services;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.models.dtos.JwtTokenDTO;
import com.api.books.services.models.dtos.LoginDTO;
import com.api.books.services.models.dtos.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JwtTokenDTO> login(LoginDTO login) throws Exception;

    ResponseEntity<ResponseDTO> register(UserEntity user) throws Exception;

    ResponseEntity<ResponseDTO> registerAdmin(UserEntity user) throws Exception;
}
