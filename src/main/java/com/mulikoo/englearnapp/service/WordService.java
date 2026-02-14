package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.WordRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class WordService {

    private final WordRepository wordRepository;
    private final CategoryRepository categoryRepository;

    public Optional<Word> findByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return Optional.empty();
        }

        return wordRepository.findByUid(uid);
    }

    @Transactional
    public Optional<Word> create(@NonNull WordDto dto) {

        Optional<Category> category = categoryRepository.findByUid(dto.getCategoryUid());

        if (category.isEmpty()) {
            throw new EntityNotFoundException("Category Not Found");
        }

        Word word = new Word();

        word.setUid(UUID.randomUUID());
        word.setName(dto.getName());
        word.setClue(dto.getClue());
        word.setTranslation(dto.getTranslation());
        word.setCategoryId(category.get().getId());
        word.setCreationDate(LocalDateTime.now());
        word.setModificationDate(LocalDateTime.now());

        return Optional.of(wordRepository.save(word));
    }

    @Transactional
    public Optional<Word> update(@NonNull UUID uid, @NonNull WordDto dto) {
        Optional<Word> currentWord = findByUid(uid);
        if (currentWord.isEmpty()) {
            throw new EntityNotFoundException("current word is null with uid " + uid);
        }

        Optional<Long> categoryIdOp = categoryRepository.findIdByUid(dto.getCategoryUid());

        if (categoryIdOp.isEmpty()) {
            throw new EntityNotFoundException("Category Not Found with uid: " + dto.getCategoryUid());
        }

        return currentWord
                .map(word -> {
                    word.setName(dto.getName());
                    word.setClue(dto.getClue());
                    word.setTranslation(dto.getTranslation());
                    word.setCategoryId(categoryIdOp.get());
                    word.setModificationDate(LocalDateTime.now());
                    return wordRepository.save(word);
                });
    }


    @Transactional
    public void deleteByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return;
        }
        wordRepository.deleteByUid(uid);
    }
}