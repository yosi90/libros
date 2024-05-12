package com.api.books.services.impl;

import com.api.books.persistence.entities.AuthorEntity;
import com.api.books.persistence.entities.RoleEntity;
import com.api.books.persistence.entities.SagaEntity;
import com.api.books.persistence.entities.UniverseEntity;
import com.api.books.persistence.entities.UserEntity;
import com.api.books.persistence.repositories.AuthorRepository;
import com.api.books.persistence.repositories.RoleRepository;
import com.api.books.persistence.repositories.SagaRepository;
import com.api.books.persistence.repositories.UniverseRepository;
import com.api.books.persistence.repositories.UserRepository;
import com.api.books.services.AuthService;
import com.api.books.services.JWTUtilityService;
import com.api.books.services.models.dtos.askers.JwtTokenDTO;
import com.api.books.services.models.dtos.askers.LoginDTO;
import com.api.books.services.models.dtos.askers.ResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTUtilityService jwtUtilityService;
    private final AuthorRepository authorRepository;
    private final UniverseRepository universeRepository;
    private final SagaRepository sagaRepository;
    
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JWTUtilityService jwtUtilityService, AuthorRepository authorRepository, UniverseRepository universeRepository, SagaRepository sagaRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtilityService = jwtUtilityService;
        this.authorRepository = authorRepository;
        this.universeRepository = universeRepository;
        this.sagaRepository = sagaRepository;
    }

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
            
            UserEntity savedUser = userRepository.save(userTemplate);

            AuthorEntity anon = new AuthorEntity();
            anon.setName("Anónimo");
            anon.setUserAuthors(savedUser);
            List<AuthorEntity> authors = new ArrayList<>();
            authors.add(anon);
            authorRepository.save(anon);

            UniverseEntity noUniverse = new UniverseEntity();
            noUniverse.setName("Sin universo");
            noUniverse.setUserUniverses(savedUser);
            noUniverse.setAuthorsUniverses(authors);
            List<UniverseEntity> universes = new ArrayList<>();
            universes.add(noUniverse);
            universeRepository.save(noUniverse);

            SagaEntity noSaga = new SagaEntity();
            noSaga.setName("Sin saga");
            noSaga.setUserSagas(savedUser);
            noSaga.setAuthorsSagas(authors);
            noSaga.setUniverseSagas(noUniverse);
            List<SagaEntity> sagas = new ArrayList<>();
            sagas.add(noSaga);
            sagaRepository.save(noSaga);

            anon.setSagasAuthors(sagas);
            noUniverse.setSagasUniverse(sagas);

            userTemplate.setAuthors(authors);
            userTemplate.setUniverses(universes);
            userTemplate.setSagas(sagas);
            userRepository.save(userTemplate);

            return Optional.of(savedUser);
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
