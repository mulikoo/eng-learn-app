package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.mapper.WordMapper;
import com.mulikoo.englearnapp.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/words")
@Tag(name = "Контроллер для слов", description = "Контроллер для управления слов")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WordController {

    private final WordMapper wordMapper;
    private final WordService wordService;

    @GetMapping("/{uid}")
    @Operation(summary = "Получение слова по uid", description = "Возвращает слово")
    public ResponseEntity<WordDto> getWord(@Parameter(description = "uid слова") @PathVariable("uid") UUID uid) {
        log.info("попытка получения слова по uid: {}", uid.toString());

        Optional<Word> result = wordService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(wordMapper.toDto(result.get()));
    }

    @PostMapping
    @Operation(summary = "Создание нового слова", description = "Создание нового слова по uid")
    public ResponseEntity<WordDto> createWord(@RequestBody WordDto wordDto) {
        log.info("создание нового слова. получили name:{}, translation: {}",
                wordDto.getName(), wordDto.getTranslation());

        Optional<Word> result = wordService.create(wordDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(wordMapper.toDto(result.get()), HttpStatus.CREATED);
    }

    @PutMapping("/{uid}")
    @Operation(summary = "Обновление слова", description = "Позволяет обновлять слово")
    public ResponseEntity<WordDto> updateWord(@Parameter(description = "uid слова")
                                              @PathVariable("uid") UUID uid, @RequestBody WordDto wordDto) {
        log.info("обновление слова по uid: {}", uid.toString());

        Optional<Word> result = wordService.update(uid, wordDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(wordMapper.toDto(result.get()));
    }

    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление слова", description = "Позволяет удалять слова")
    public ResponseEntity<WordDto> deleteWord(@Parameter(description = "uid категории") @PathVariable("uid") UUID uid) {
        log.info("удаление слова по uid: {}", uid.toString());

        wordService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }

}