package com.api.books.controllers;

import com.api.books.persistence.entities.UserEntity;
import com.api.books.services.AuthService;
import com.api.books.services.UserService;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/user")
public class UserControllers {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;


    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    /*@GetMapping("/{userId}/books")
    public ResponseEntity<List<BookDTO>> getUserDepartures(@PathVariable Long userId) {
        return userService.getUserBooks(userId);
    }*/

    @PostMapping
    public ResponseEntity<ResponseDTO> createUser(@RequestBody @Valid UserEntity userNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors()) {
            ResponseDTO response = new ResponseDTO();
            for (FieldError error : result.getFieldErrors())
                response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        return authService.registerAdmin(userNew);
    }

    @PatchMapping("/{userId}/name")
    public ResponseEntity<ResponseDTO> updateName(@PathVariable Long userId, @RequestBody String nameNew) {
        return userService.updateName(userId, nameNew);
    }

    @PatchMapping("/{userId}/email")
    public ResponseEntity<ResponseDTO> updateEmail(@PathVariable Long userId, @RequestBody String emailNew) {
        return userService.updateEmail(userId, emailNew);
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<ResponseDTO> updatePassword(@PathVariable Long userId, @RequestBody String passwordNew) {
        return userService.updatePassword(userId, passwordNew);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO> removeUser(@PathVariable Long userId) {
        return userService.removeUser(userId);
    }
}
