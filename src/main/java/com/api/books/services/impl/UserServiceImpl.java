package com.api.books.services.impl;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.SagaRepository;
import com.api.books.persistence.repositories.UniverseRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.UserService;
import com.api.books.services.models.dtos.AuthorDTO;
import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.SagaDTO;
import com.api.books.services.models.dtos.UniverseDTO;
import com.api.books.services.models.dtos.UserRolesDTO;
import com.api.books.services.models.dtos.askers.PasswordUpdateDTO;
import com.api.books.services.models.dtos.askers.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final UniverseRepository universeRepository;
    private final SagaRepository sagaRepository;
    private final BookRepository bookRepository;

    public UserServiceImpl(UserRepository userRepository, AuthorRepository authorRepository, UniverseRepository universeRepository, SagaRepository sagaRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.universeRepository = universeRepository;
        this.sagaRepository = sagaRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(user.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<UserRolesDTO> getUserRoles(Long userId) {
        try {
            if (!isADMIN())
                return new ResponseEntity<>(new UserRolesDTO(), HttpStatus.UNAUTHORIZED);
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(user.toRolesDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<AuthorDTO>> getUserAuthors(Long userId) {
        try {
            List<AuthorEntity> authors = authorRepository.findByUserAuthorsId(userId);
            List<AuthorDTO> authorDTOs = new ArrayList<>();
            for (AuthorEntity author : authors)
                authorDTOs.add(author.toDTO());
            return ResponseEntity.ok(authorDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<UniverseDTO>> getUserUniverses(Long userId) {
        try {
            List<UniverseEntity> universes = universeRepository.findByUserUniversesId(userId);
            List<UniverseDTO> universeDTOs = new ArrayList<>();
            for (UniverseEntity universe : universes)
                universeDTOs.add(universe.toDTO());
            return ResponseEntity.ok(universeDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<SagaDTO>> getUserSagas(Long userId) {
        try {
            List<SagaEntity> sagas = sagaRepository.findByUserSagasId(userId);
            List<SagaDTO> sagaDTOs = new ArrayList<>();
            for (SagaEntity saga : sagas)
                sagaDTOs.add(saga.toDTO());
            return ResponseEntity.ok(sagaDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> getUserBooks(Long userId) {
        try {
            List<BookEntity> books = bookRepository.findByOwnerId(userId);
            List<BookDTO> bookDTOs = new ArrayList<>();
            for (BookEntity book : books)
                bookDTOs.add(book.toDTO());
            return ResponseEntity.ok(bookDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<UserRolesDTO>> getAllUsers() {
        try {
            if (!isADMIN())
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            List<UserEntity> users = userRepository.findAll();
            List<UserRolesDTO> userDTOS = new ArrayList<>();
            if (users.isEmpty())
                return ResponseEntity.noContent().build();
            for (UserEntity userEntity : users)
                userDTOS.add(userEntity.toRolesDTO());
            return ResponseEntity.ok(userDTOS);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<ResponseDTO> updateUser(Long id, UserEntity updatedUser) {
        ResponseDTO response = new ResponseDTO();
        try {
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setLifeSpan(updatedUser.getLifeSpan().plusYears(100));
            previousUser.setName(updatedUser.getName());
            previousUser.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword()
                    .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$")) {
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
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
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
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
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
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            if (!passwords.getPasswordNew()
                    .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$")
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
            if (!isADMIN())
                return new ResponseEntity<>(new ResponseDTO("No tienes permiso"), HttpStatus.UNAUTHORIZED);
            userRepository.deleteById(userId);
            response.newMessage("Usuario borrado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.newError(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public boolean isADMIN() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities)
                if ("ADMIN".equals(authority.getAuthority()))
                    return true;
        }
        return false;
    }
}