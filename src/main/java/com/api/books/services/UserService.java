package com.api.books.services;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.UserRolesDTO;
import com.api.books.services.models.dtos.templates.PasswordUpdateDTO;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<UserDTO> getUserById(Long userId);

    ResponseEntity<UserRolesDTO> getUserRoles(Long userId);

    ResponseEntity<List<AuthorDTO>> getUserAuthors(Long userId);

    ResponseEntity<List<UniverseDTO>> getUserUniverses(Long userId);

    ResponseEntity<List<SagaDTO>> getUserSagas(Long userId);

    ResponseEntity<List<BookDTO>> getUserBooks(Long userId);

    ResponseEntity<List<UserRolesDTO>> getAllUsers();

    ResponseEntity<ResponseDTO> updateUser(Long id, UserEntity updatedUser);

    ResponseEntity<UserDTO> updateName(Long id, String nameNew);

    ResponseEntity<UserDTO> updateEmail(Long id, String emailNew);

    ResponseEntity<UserDTO> updatePassword(Long id, PasswordUpdateDTO passwords);

    ResponseEntity<ResponseDTO> removeUser(Long userId);

    boolean isADMIN();
}
