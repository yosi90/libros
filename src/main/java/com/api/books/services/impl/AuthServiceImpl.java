package com.api.books.services.impl;

import com.api.books.persistence.entities.RoleEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.RoleRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.AuthService;
import com.api.books.services.JWTUtilityService;
import com.api.books.services.UserService;
import com.api.books.services.models.dtos.LoginDTO;
import com.api.books.services.models.dtos.ResponseDTO;
import com.api.books.services.models.dtos.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JWTUtilityService jwtUtilityService;

    @Override
    public ResponseEntity<String> login(LoginDTO login) throws Exception {
        try {
            Optional<UserEntity> userOPT = userRepository.findByEmail(login.getEmail());
            if (userOPT.isEmpty())
                return ResponseEntity.notFound().build();
            else {
                UserEntity user = userOPT.get();
                if (verifyPassword(login.getPassword(), user.getPassword())) {
                    final String jwtTokenDTO = jwtUtilityService.generateJWT(user.getId(),  user.getRoles());
                    return ResponseEntity.ok(jwtTokenDTO);
                } else
                    return ResponseEntity.internalServerError().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }

    @Override
    public ResponseEntity<ResponseDTO> register(UserEntity userNew) {
        try {
            ResponseDTO response = new ResponseDTO();
            Optional<UserEntity> existingUser = userRepository.findByName(userNew.getName());
            if (existingUser.isPresent()) {
                response.newError("Nombre en uso, por favor elija otro");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            existingUser = userRepository.findByEmail(userNew.getEmail());
            if (existingUser.isPresent()) {
                response.newError("Email ya registrado");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            UserEntity userTEMP = getTemplateUser();
            Optional<UserEntity> userOPT = updateTemplateUser(userTEMP.getId(), userNew);
            if (userOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            response.newMessage("Usuario creado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> registerAdmin(UserEntity userNew) {
        try {
            ResponseDTO response = new ResponseDTO();
            Optional<UserEntity> existingUser = userRepository.findByName(userNew.getName());
            if (existingUser.isPresent()) {
                response.newError("Nombre en uso, por favor elija otro");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            existingUser = userRepository.findByEmail(userNew.getEmail());
            if (existingUser.isPresent()) {
                response.newError("Email ya registrado");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            UserEntity userTEMP = getTemplateUser();
            Optional<UserEntity> userOPT = updateTemplateAdmin(userTEMP.getId(), userNew);
            if (userOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            response.newMessage("Usuario creado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private UserEntity getTemplateUser() {
        int threshold = 3600;
        Optional<UserEntity> userUpdatable = userRepository.findFirstUpdatable(threshold);
        if (userUpdatable.isEmpty()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setName("NombreNoValido");
            userEntity.setEmail("emailn@oval.ido");
            userEntity.setPassword("ContraseñaNoValida");
            userEntity = userRepository.save(userEntity);
            return userEntity;
        } else return userUpdatable.get();
    }

    private Optional<UserEntity> updateTemplateUser(Long id, UserEntity updatedUser) {
        ResponseDTO response = new ResponseDTO();
        try {
            UserEntity previousUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setLifeSpan(updatedUser.getLifeSpan().plusYears(100));
            previousUser.setName(updatedUser.getName());
            previousUser.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$"))
                return Optional.empty();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            previousUser.setPassword(encoder.encode(updatedUser.getPassword()));
            Optional<RoleEntity> roleOTP = roleRepository.findByName("USER");
            if(roleOTP.isEmpty())
                return Optional.empty();
            List<RoleEntity> roles = new ArrayList<>();
            roles.add(roleOTP.get());
            previousUser.setRoles(roles);
            UserEntity userFinal = userRepository.save(previousUser);
            return Optional.of(userFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<UserEntity> updateTemplateAdmin(Long id, UserEntity updatedUser) {
        ResponseDTO response = new ResponseDTO();
        try {
            UserEntity previousUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setLifeSpan(updatedUser.getLifeSpan().plusYears(100));
            previousUser.setName(updatedUser.getName());
            previousUser.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$")) {
                return Optional.empty();
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            previousUser.setPassword(encoder.encode(updatedUser.getPassword()));
            List<RoleEntity> roles = roleRepository.findAll();
            previousUser.setRoles(roles);
            UserEntity userFinal = userRepository.save(previousUser);
            return Optional.of(userFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
