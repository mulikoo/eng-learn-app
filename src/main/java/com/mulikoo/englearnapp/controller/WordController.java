package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.mapper.WordMapper;
import com.mulikoo.englearnapp.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/words")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WordController {

    private final WordMapper wordMapper;
    private final WordService wordService;

    @GetMapping("/{uid}")
    public ResponseEntity<WordDto> getWord(@PathVariable("uid") UUID uid) {
        log.info("попытка получения слова по uid: {}", uid.toString());

        Optional<Word> result = wordService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(wordMapper.toDto(result.get()));
    }

    @PostMapping
    public ResponseEntity<WordDto> createWord(@RequestBody WordDto wordDto) {
        log.info("создание нового слова. получили name:{}, translation: {}",
                wordDto.getName(), wordDto.getTranslation());

        var mockResponse = wordDto;
        mockResponse.setUid(UUID.randomUUID());
        mockResponse.setCreationDate(LocalDateTime.now());
        mockResponse.setModificationDate(LocalDateTime.now());

        return new ResponseEntity<>(mockResponse, HttpStatus.CREATED); //вернули исходящую dto
    }

    @PutMapping("/{uid}")
    public ResponseEntity<WordDto> updateWord(@PathVariable("uid") UUID uid, @RequestBody WordDto wordDto) {
        log.info("обновление слова по uid: {}", uid.toString());

        var mockResponse = wordDto;
        mockResponse.setUid(uid);
        mockResponse.setCreationDate(LocalDateTime.now().minusDays(2));
        mockResponse.setModificationDate(LocalDateTime.now());

        return ResponseEntity.ok(mockResponse);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteWord(@PathVariable("uid") UUID uid) {
        log.info("удаление слова по uid: {}", uid.toString());

        return ResponseEntity.ok().build();
    }

}