package com.api.books.controllers;

import com.api.books.services.ChapterService;
import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.CharacterDTO;
import com.api.books.services.models.dtos.templates.NewChapter;
import com.api.books.services.models.dtos.templates.NewCharacter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chapter")
public class ChapterControllers {

    @Autowired
    ChapterService chapterService;

    @PostMapping
    public ResponseEntity<ChapterDTO> addChapter(@RequestBody @Valid NewChapter chapterNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new ChapterDTO(), HttpStatus.NOT_ACCEPTABLE);
        return chapterService.addChapter(chapterNew);
    }

    @PutMapping("/{chapterId}")
    public ResponseEntity<ChapterDTO> updateCharacter(@PathVariable Long chapterId, @RequestBody @Valid NewChapter chapterNew, BindingResult result) throws Exception {
        if (result != null && result.hasErrors())
            return new ResponseEntity<>(new ChapterDTO(), HttpStatus.NOT_ACCEPTABLE);
        return chapterService.updateChapter(chapterId, chapterNew);
    }
}
