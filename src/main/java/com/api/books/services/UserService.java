package com.api.books.services;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<UserDTO> getUserById(Long userId);
    ResponseEntity<List<UserDTO>> getAllUsers();
    ResponseEntity<List<BookDTO>> getBooks(Long userId);
    ResponseEntity<ResponseDTO> update(Long id, UserEntity updatedUser);
    ResponseEntity<ResponseDTO> updateName(Long id, String nameNew);
    ResponseEntity<ResponseDTO> updateEmail(Long id, String emailNew);
    ResponseEntity<ResponseDTO> updatePassword(Long id, String newPassword);
    ResponseEntity<ResponseDTO> removeUser(Long userId);
}
