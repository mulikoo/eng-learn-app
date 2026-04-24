package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.enums.WordSortField;
import com.mulikoo.englearnapp.mapper.WordMapper;
import com.mulikoo.englearnapp.service.WordService;
import com.nimbusds.jwt.JWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PreAuthorize("hasAuthority('WORD_READ')")
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

    @PreAuthorize("hasAuthority('WORD_READ')")
    @GetMapping
    @Operation(summary = "Получение списка слов", description = "Возвращает список слов")
    public ResponseEntity<Page<WordDto>> getAllWord(@RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
                                                    @RequestParam(name = "size", defaultValue = "10") @Min(1) int size,
                                                    @RequestParam(name = "sortField", defaultValue = "NAME") WordSortField sortField,
                                                    @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        log.info("Попытка получения списка слов");

        Page<Word> wordPage = wordService.findAll(page, size, sortField, sortDirection);
        Page<WordDto> wordDtoPage = wordPage.map(wordMapper::toDto);

        return ResponseEntity.ok(wordDtoPage);
    }

    @PreAuthorize("hasAuthority('WORD_LEARN')")
    @GetMapping("/next")
    @Operation(summary = "Получение неизученного слова",
            description = "Получает следующее неизученное слово и регистрирует его отправку")
    public ResponseEntity<WordDto> getNextWord(@AuthenticationPrincipal Jwt token) {
        log.info("Попытка получения неизученного слова для пользователя: {}", token.getSubject());

        Optional<Word> nextWord = wordService.findNextWord(token.getSubject());
        if (nextWord.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(wordMapper.toDto(nextWord.get()));
    }

    @PreAuthorize("hasAuthority('WORD_LEARN')")
    @GetMapping("/checkTranslation")
    @Operation(summary = "Проверка перевода слова",
            description = "Получает перевод от пользователя и сравнивает с фактическим переводом")
    public ResponseEntity<Boolean> comparisonTranslation(@RequestParam("uid") UUID uid,
                                                         @RequestParam("translation") String translation,
                                                         @AuthenticationPrincipal Jwt token) {
        log.info("Проверка перевода слова с uid {} от пользователя: {}", uid, token.getSubject());

        boolean isCorrect = wordService.isTranslationCorrect(uid, translation, token.getSubject());

        return ResponseEntity.ok(isCorrect);

    }

    @PreAuthorize("hasAuthority('WORD_LEARN')")
    @GetMapping("/getClue")
    @Operation(summary = "Текстовая подсказка для пользователя", description = "Получает подсказку по uid и username")
    public ResponseEntity<String> getTextClue(@RequestParam("uid") UUID uid, @AuthenticationPrincipal Jwt token) {
        log.info("Получение текстовой подсказки по uid слова {} и username{} ", uid, token.getSubject());

        String result = wordService.getClue(uid, token.getSubject());

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('WORD_CREATE')")
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

    @PreAuthorize("hasAuthority('WORD_UPDATE')")
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

    @PreAuthorize("hasAuthority('WORD_DELETE')")
    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление слова", description = "Позволяет удалять слова")
    public ResponseEntity<WordDto> deleteWord(@Parameter(description = "uid категории") @NotNull @PathVariable("uid") UUID uid) {
        log.info("удаление слова по uid: {}", uid.toString());

        wordService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('WORD_LEARN')")
    @GetMapping("/current")
    @Operation(summary = "Получение слова, которое сейчас на изучении", description = "Слово которое на изучении")
    public ResponseEntity<WordDto> getCurrentWord(@AuthenticationPrincipal Jwt token) {
        log.info("получение слова, которое сейчас в процессе изучения у пользователя: {}", token.getSubject());

        Optional<Word> result = wordService.getCurrentLearningWord(token.getSubject());
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(wordMapper.toDto(result.get()));
    }
}