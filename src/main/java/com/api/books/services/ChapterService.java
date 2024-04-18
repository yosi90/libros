package com.api.books.services;

import com.api.books.services.models.dtos.ChapterDTO;
import com.api.books.services.models.dtos.templates.NewChapter;
import org.springframework.http.ResponseEntity;

public interface ChapterService {

    ResponseEntity<ChapterDTO> addChapter(NewChapter chapterNew);

    ResponseEntity<ChapterDTO> updateChapter(Long id, NewChapter updatedChapter);
}
