package com.api.books.services.impl;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.UserService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.templates.PasswordUpdateDTO;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(user.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserEntity> users = userRepository.findAll();
            List<UserDTO> userDTOS = new ArrayList<>();
            if (users.isEmpty()) return ResponseEntity.noContent().build();
            for (UserEntity userEntity : users)
                userDTOS.add(userEntity.toDTO());
            return ResponseEntity.ok(userDTOS);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> getBooks(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return ResponseEntity.notFound().build();
            if (user.getBooks().isEmpty())
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok(user.getBooksDTOs());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> update(Long id, UserEntity updatedUser) {
        ResponseDTO response = new ResponseDTO();
        try {
            UserEntity previousUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setLifeSpan(updatedUser.getLifeSpan().plusYears(100));
            previousUser.setName(updatedUser.getName());
            previousUser.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$")) {
                response.newError("Contraseña no válida");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            previousUser.setPassword(encoder.encode(updatedUser.getPassword()));
            userRepository.save(previousUser);
            response.newMessage("Usuario actualizado");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.newError("Id no encontrada");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            response.newError(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateName(Long id, String nameNew) {
        try {
            UserEntity previousUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setName(nameNew);
            final UserEntity user = userRepository.save(previousUser);
            return ResponseEntity.ok(user.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateEmail(Long id, String emailNew) {
        try {
            UserEntity previousUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setEmail(emailNew);
            final UserEntity user = userRepository.save(previousUser);
            return ResponseEntity.ok(user.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<UserDTO> updatePassword(Long id, PasswordUpdateDTO passwords) {
        try {
            UserEntity previousUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            if (!passwords.getPasswordNew().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$")
                    || !verifyPassword(passwords.getPasswordOld(), previousUser.getPassword()))
                return new ResponseEntity<>(new UserDTO(), HttpStatus.NOT_ACCEPTABLE);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            previousUser.setPassword(encoder.encode(passwords.getPasswordNew()));
            final UserEntity user = userRepository.save(previousUser);
            return ResponseEntity.ok(user.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }

    @Override
    public ResponseEntity<ResponseDTO> removeUser(Long userId) {
        ResponseDTO response = new ResponseDTO();
        try {
            userRepository.deleteById(userId);
            response.newMessage("Usuario borrado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.newError(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }
}