package com.api.books.services.impl;

import com.api.books.persistence.entities.BookEntity;
import com.api.books.persistence.entities.ChapterEntity;
import com.api.books.persistence.entities.CharacterEntity;
import com.api.books.persistence.repositories.BookRepository;
import com.api.books.persistence.repositories.ChapterRepository;
import com.api.books.persistence.repositories.CharacterRepository;
import com.api.books.services.ChapterService;
import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import com.api.books.services.models.dtos.templates.NewChapter;
import com.api.books.services.models.dtos.templates.NewCharacter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    private final BookRepository bookRepository;

    private final CharacterRepository characterRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository, BookRepository bookRepository, CharacterRepository characterRepository) {
        this.chapterRepository = chapterRepository;
        this.bookRepository = bookRepository;
        this.characterRepository = characterRepository;
    }

    @Override
    public ResponseEntity<ChapterDTO> addChapter(NewChapter chapterNew) {
        try {
            Optional<ChapterEntity> existingChapter = chapterRepository.findByName(chapterNew.getName());
            if (existingChapter.isPresent())
                return new ResponseEntity<>(new ChapterDTO(), HttpStatus.CONFLICT);
            Optional<BookEntity> existingBook = bookRepository.findById(chapterNew.getBookId());
            if (existingBook.isEmpty())
                return new ResponseEntity<>(new ChapterDTO(), HttpStatus.NOT_FOUND);
            List<CharacterEntity> characters = new ArrayList<>();
            for(Long characterId : chapterNew.getCharactersId()) {
                CharacterEntity character = characterRepository.findById(characterId).orElseThrow(() -> new EntityNotFoundException("Personaje no encontrado"));
                characters.add(character);
            }
            ChapterEntity chapterTEMP = getTemplateChapter(existingBook.get());
            Optional<ChapterEntity> chapterOPT = updateTemplateChapter(chapterTEMP, characters, chapterNew);
            if (chapterOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            return ResponseEntity.ok(chapterOPT.get().toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private ChapterEntity getTemplateChapter(BookEntity book) {
        Optional<ChapterEntity> chapterTEMP = chapterRepository.findByName("chapterTemplate");
        if (chapterTEMP.isEmpty()) {
            ChapterEntity chapterEntity = new ChapterEntity();
            chapterEntity.setName("chapterTemplate");
            chapterEntity.setDescription("Esto es una descripción no válida");
            chapterEntity.setOrderInBook(0);
            chapterEntity.setOrigin(book);
            chapterEntity = chapterRepository.save(chapterEntity);
            return chapterEntity;
        }
        return chapterTEMP.get();
    }

    private Optional<ChapterEntity> updateTemplateChapter(ChapterEntity chapterTemplate, List<CharacterEntity> characters, NewChapter updatedCharacter) {
        try {
            chapterTemplate.setName(updatedCharacter.getName());
            chapterTemplate.setDescription(updatedCharacter.getDescription());
            chapterTemplate.setOrderInBook(updatedCharacter.getOrderInBook());
            chapterTemplate.setCharacters(characters);
            ChapterEntity chapterFinal = chapterRepository.save(chapterTemplate);
            return Optional.of(chapterFinal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<ChapterDTO> updateChapter(Long id, NewChapter updatedChapter) {
        try {
            ChapterEntity previousChapter = chapterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Capítulo no encontrado"));
            previousChapter.setName(updatedChapter.getName());
            previousChapter.setDescription(updatedChapter.getDescription());
            previousChapter.setOrderInBook(updatedChapter.getOrderInBook());
            List<CharacterEntity> characters = new ArrayList<>();
            for(Long characterId : updatedChapter.getCharactersId()) {
                CharacterEntity character = characterRepository.findById(characterId).orElseThrow(() -> new EntityNotFoundException("Personaje no encontrado"));
                characters.add(character);
            }
            previousChapter.setCharacters(characters);
            ChapterEntity characterFinal = chapterRepository.save(previousChapter);
            return ResponseEntity.ok(characterFinal.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
