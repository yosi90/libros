package com.api.books.services.impl;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.CharacterEntity;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.CharacterRepository;
import com.api.books.services.CharacterService;
import com.api.books.services.models.dtos.CharacterDTO;
import com.api.books.services.models.dtos.askers.NewCharacter;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final BookRepository bookRepository;

    public CharacterServiceImpl(CharacterRepository characterRepository, BookRepository bookRepository) {
        this.characterRepository = characterRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseEntity<CharacterDTO> getCharacterById(Long characterId) {
        try {
            CharacterEntity character = characterRepository.findById(characterId).orElse(null);
            if (character == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(character.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        try {
            List<CharacterEntity> characters = characterRepository.findAll();
            List<CharacterDTO> characterDTOS = new ArrayList<>();
            if (characters.isEmpty()) return ResponseEntity.noContent().build();
            for (CharacterEntity characterEntity : characters)
                characterDTOS.add(characterEntity.toDTO());
            return ResponseEntity.ok(characterDTOS);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<CharacterDTO> addCharacter(NewCharacter characterNew) {
        try {
            Optional<CharacterEntity> existingCharacter = characterRepository.findByName(characterNew.getName());
            if (existingCharacter.isPresent())
                return new ResponseEntity<>(new CharacterDTO(), HttpStatus.CONFLICT);
            Optional<BookEntity> existingBook = bookRepository.findById(characterNew.getBookId());
            if (existingBook.isEmpty())
                return new ResponseEntity<>(new CharacterDTO(), HttpStatus.NOT_FOUND);
            CharacterEntity characterTEMP = getTemplateCharacter(existingBook.get());
            Optional<CharacterEntity> characterOPT = updateTemplateCharacter(characterTEMP, characterNew);
            if (characterOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            return ResponseEntity.ok(characterOPT.get().toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private CharacterEntity getTemplateCharacter(BookEntity book) {
        Optional<CharacterEntity> characterTEMP = characterRepository.findByName("characterTemplate");
        if (characterTEMP.isEmpty()) {
            CharacterEntity characterEntity = new CharacterEntity();
            characterEntity.setName("characterTemplate");
            characterEntity.setDescription("Esto es una descripción no válida");
            characterEntity.setBookCharacters(book);
            characterEntity = characterRepository.save(characterEntity);
            return characterEntity;
        }
        return characterTEMP.get();
    }

    private Optional<CharacterEntity> updateTemplateCharacter(CharacterEntity characterTemplate, NewCharacter updatedCharacter) {
        try {
            characterTemplate.setName(updatedCharacter.getName());
            characterTemplate.setDescription(updatedCharacter.getDescription());
            CharacterEntity characterFinal = characterRepository.save(characterTemplate);
            return Optional.of(characterFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<CharacterDTO> updateCharacter(Long id, NewCharacter updatedCharacter) {
        try {
            CharacterEntity previousCharacter = characterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Personaje no encontrado"));
            previousCharacter.setName(updatedCharacter.getName());
            previousCharacter.setDescription(updatedCharacter.getDescription());
            CharacterEntity characterFinal = characterRepository.save(previousCharacter);
            return ResponseEntity.ok(characterFinal.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
