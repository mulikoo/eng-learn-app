package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.enums.WordSortField;
import com.mulikoo.englearnapp.mapper.WordMapper;
import com.mulikoo.englearnapp.repository.WordRepository;
import com.mulikoo.englearnapp.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/words")
@Tag(name = "Контроллер для слов", description = "Контроллер для управления слов")
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class WordController {

    private final WordMapper wordMapper;
    private final WordService wordService;
    private final WordRepository wordRepository;

    @GetMapping("/{uid}")
    @Operation(summary = "Получение слова по uid", description = "Возвращает слово")
    public ResponseEntity<WordDto> getWord(@Parameter(description = "uid слова") @NotNull @PathVariable("uid") UUID uid) {
        log.info("попытка получения слова по uid: {}", uid.toString());

        Optional<Word> result = wordService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(wordMapper.toDto(result.get()));
    }

    @GetMapping
    @Operation(summary = "Получение списка слов", description = "Возвращает список слов")
    public ResponseEntity<Page<WordDto>> getAllWord(@RequestParam(name = "page", defaultValue = "0") int page,
                                                    @RequestParam(name = "size", defaultValue = "10") int size,
                                                    @RequestParam(name = "sortField", defaultValue = "NAME") WordSortField sortField,
                                                    @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        log.info("Попытка получения списка слов");

        Sort sort = Sort.by(sortDirection, sortField.getFieldName());

        Page<Word> wordPage = wordRepository.findAll(PageRequest.of(page, size, sort));
        Page<WordDto> wordDtoPage = wordPage.map(wordMapper::toDto);

        return ResponseEntity.ok(wordDtoPage);
    }

    @PostMapping
    @Operation(summary = "Создание нового слова", description = "Создание нового слова по uid")
    public ResponseEntity<WordDto> createWord(@Validated @RequestBody WordDto wordDto) {
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
                                              @NotNull @PathVariable("uid") UUID uid, @Validated @RequestBody WordDto wordDto) {
        log.info("обновление слова по uid: {}", uid.toString());

        Optional<Word> result = wordService.update(uid, wordDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(wordMapper.toDto(result.get()));
    }

    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление слова", description = "Позволяет удалять слова")
    public ResponseEntity<WordDto> deleteWord(@Parameter(description = "uid категории") @NotNull @PathVariable("uid") UUID uid) {
        log.info("удаление слова по uid: {}", uid.toString());

        wordService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }

}