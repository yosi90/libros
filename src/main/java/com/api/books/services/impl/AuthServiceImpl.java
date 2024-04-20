package com.api.books.services.impl;

import com.api.books.persistence.entities.RoleEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.RoleRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.AuthService;
import com.api.books.services.JWTUtilityService;
import com.api.books.services.UserService;
import com.api.books.services.models.dtos.templates.JwtTokenDTO;
import com.api.books.services.models.dtos.templates.LoginDTO;
import com.api.books.services.models.dtos.templates.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
    public ResponseEntity<JwtTokenDTO> login(LoginDTO login) throws Exception {
        try {
            Optional<UserEntity> userOPT = userRepository.findByEmail(login.getEmail());
            if (userOPT.isEmpty())
                return ResponseEntity.notFound().build();
            else {
                UserEntity user = userOPT.get();
                if (verifyPassword(login.getPassword(), user.getPassword())) {
                    JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(jwtUtilityService.generateJWT(user.getId(),  user.getRoles()));
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
    public ResponseEntity<ResponseDTO> register(UserEntity userNew, BindingResult result) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (result != null && result.hasErrors()) {
                for (FieldError error : result.getFieldErrors())
                    response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
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
            Optional<UserEntity> userOPT = updateTemplateUser(userTEMP, userNew);
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
        Optional<UserEntity> userTEMP = userRepository.findFirstUpdatable(threshold);
        if (userTEMP.isEmpty()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setName("NombreNoValido");
            userEntity.setEmail("emailn@oval.ido");
            userEntity.setPassword("ContraseñaNoValida");
            userEntity = userRepository.save(userEntity);
            return userEntity;
        }
        return userTEMP.get();
    }

    private Optional<UserEntity> updateTemplateUser(UserEntity userTemplate, UserEntity updatedUser) {
        try {
            userTemplate.setLifeSpan(updatedUser.getLifeSpan().plusYears(100));
            userTemplate.setName(updatedUser.getName());
            userTemplate.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$"))
                return Optional.empty();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            userTemplate.setPassword(encoder.encode(updatedUser.getPassword()));
            Optional<RoleEntity> roleOTP = roleRepository.findByName("USER");
            if(roleOTP.isEmpty())
                return Optional.empty();
            List<RoleEntity> roles = new ArrayList<>();
            roles.add(roleOTP.get());
            userTemplate.setRoles(roles);
            UserEntity userFinal = userRepository.save(userTemplate);
            return Optional.of(userFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> registerAdmin(UserEntity userNew, BindingResult result) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (result != null && result.hasErrors()) {
                for (FieldError error : result.getFieldErrors())
                    response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
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
            Optional<UserEntity> userOPT = updateTemplateAdmin(userTEMP, userNew);
            if (userOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            response.newMessage("Usuario creado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private Optional<UserEntity> updateTemplateAdmin(UserEntity userTemplate, UserEntity updatedUser) {
        ResponseDTO response = new ResponseDTO();
        try {
            userTemplate.setLifeSpan(updatedUser.getLifeSpan().plusYears(100));
            userTemplate.setName(updatedUser.getName());
            userTemplate.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$")) {
                return Optional.empty();
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            userTemplate.setPassword(encoder.encode(updatedUser.getPassword()));
            List<RoleEntity> roles = roleRepository.findAll();
            userTemplate.setRoles(roles);
            UserEntity userFinal = userRepository.save(userTemplate);
            return Optional.of(userFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
