package com.api.books.services;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.models.dtos.LoginDTO;
import com.api.books.services.models.dtos.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<UserDTO> login(LoginDTO login) throws Exception;

    ResponseEntity<String> loginSimple(LoginDTO login) throws Exception;

    ResponseEntity<ResponseDTO> register(UserEntity user) throws Exception;
}
