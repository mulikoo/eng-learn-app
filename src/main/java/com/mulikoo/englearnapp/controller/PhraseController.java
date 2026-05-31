package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.PhraseDto;
import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Phrase;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.enums.WordSortField;
import com.mulikoo.englearnapp.mapper.PhraseMapper;
import com.mulikoo.englearnapp.service.PhraseService;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/phrase")
@Tag(name = "Контроллер для фраз", description = "Контроллер для управления фразами")
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class PhraseController {
    private final PhraseMapper phraseMapper;
    private final PhraseService phraseService;

    @PreAuthorize("hasAuthority('PHRASE_READ')")
    @GetMapping("/{uid}")
    public ResponseEntity<PhraseDto> getPhrase(@Parameter(description = "uid фразы") @NotNull @PathVariable("uid") UUID uid) {
        log.info("попытка получения фразы по uid: {}", uid.toString());

        Optional<Phrase> result = phraseService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(phraseMapper.toDto(result.get()));
    }

    @PreAuthorize("hasAuthority('PHRASE_LEARN')")
    @GetMapping("/next")
    @Operation(summary = "Получение неизученной фразы ",
            description = "Получает следующей неизученной фразы и регистрирует его отправку")
    public ResponseEntity<PhraseDto> getNextPhrase(@AuthenticationPrincipal Jwt token) {
        log.info("Попытка получения неизученного слова для пользователя: {}", token.getSubject());

        Optional<Phrase> nextWord = phraseService.findNextPhrase(token.getSubject());
        if (nextWord.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(phraseMapper.toDto(nextWord.get()));
    }

    @PreAuthorize("hasAuthority('PHRASE_LEARN')")
    @GetMapping("/checkTranslation")
    @Operation(summary = "Проверка перевода фразы",
            description = "Получает перевод от пользователя и сравнивает с фактическим переводом")
    public ResponseEntity<Boolean> comparisonTranslation(@RequestParam("uid") UUID uid,
                                                         @RequestParam("translation") String translation,
                                                         @AuthenticationPrincipal Jwt token) {
        log.info("Проверка перевода слова с uid {} от пользователя: {}", uid, token.getSubject());

        boolean isCorrect = phraseService.isTranslationCorrect(uid, translation, token.getSubject());

        return ResponseEntity.ok(isCorrect);

    }

    @PreAuthorize("hasAuthority('PHRASE_LEARN')")
    @GetMapping("/getClue")
    @Operation(summary = "Текстовая подсказка для пользователя", description = "Получает подсказку по uid и username")
    public ResponseEntity<String> getTextClue(@RequestParam("uid") UUID uid, @AuthenticationPrincipal Jwt token) {
        log.info("Получение текстовой подсказки по uid фразы {} и username{} ", uid, token.getSubject());

        String result = phraseService.getClue(uid, token.getSubject());

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('PHRASE_CREATE')")
    @PostMapping
    @Operation(summary = "Создание новой фразы", description = "Создание новой фразы по uid")
    public ResponseEntity<PhraseDto> createPhrase(@Validated @RequestBody PhraseDto phraseDto) {
        log.info("создание новой фразы. получили name:{}, translation: {}",
                phraseDto.getName(), phraseDto.getTranslation());

        Optional<Phrase> result = phraseService.create(phraseDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(phraseMapper.toDto(result.get()), HttpStatus.CREATED);
    }


    @PreAuthorize("hasAuthority('PHRASE_UPDATE')")
    @PutMapping("/{uid}")
    @Operation(summary = "Обновление фразы", description = "Позволяет обновлять фразу")
    public ResponseEntity<PhraseDto> updatePhrase(@Parameter(description = "uid фразы")
                                                  @NotNull @PathVariable("uid") UUID uid, @Validated @RequestBody PhraseDto phraseDto) {
        log.info("обновление фразы по uid: {}", uid.toString());

        Optional<Phrase> result = phraseService.update(uid, phraseDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(phraseMapper.toDto(result.get()));
    }

    @PreAuthorize("hasAuthority('PHRASE_DELETE')")
    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление фразы", description = "Позволяет удалять фразу")
    public ResponseEntity<PhraseDto> deletePhrase(@NotNull @PathVariable("uid") UUID uid) {
        log.info("удаление фразы по uid: {}", uid.toString());

        phraseService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }
}
