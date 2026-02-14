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

        Optional<Word> result = wordService.create(wordDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(wordMapper.toDto(result.get()), HttpStatus.CREATED);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<WordDto> updateWord(@PathVariable("uid") UUID uid, @RequestBody WordDto wordDto) {
        log.info("обновление слова по uid: {}", uid.toString());

        Optional<Word> result = wordService.update(uid, wordDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(wordMapper.toDto(result.get()));
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<WordDto> deleteWord(@PathVariable("uid") UUID uid) {
        log.info("удаление слова по uid: {}", uid.toString());

        wordService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }

}