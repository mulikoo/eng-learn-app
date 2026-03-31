package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.enums.WordSortField;
import com.mulikoo.englearnapp.exceptions.EntityAlreadyExistsException;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.WordRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class WordService {

    private final WordRepository wordRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final UserProgressService userProgressService;

    public Optional<Word> findByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return Optional.empty();
        }

        return wordRepository.findByUid(uid);
    }

    @Transactional
    public Optional<Word> create(@NonNull WordDto dto) {

        if (wordRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())) {
            throw new EntityAlreadyExistsException("word уже существует");
        }

        Category category = categoryRepository.findByUid(dto.getCategoryUid())
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found"));

        Word word = new Word();

        word.setUid(UUID.randomUUID());
        word.setName(dto.getName());
        word.setClue(dto.getClue());
        word.setTranslation(dto.getTranslation());
        word.setCategory(category);

        return Optional.of(wordRepository.save(word));
    }

    @Transactional
    public Optional<Word> update(@NonNull UUID uid, @NonNull WordDto dto) {

        Optional<Word> currentWord = findByUid(uid);
        if (currentWord.isEmpty()) {
            throw new EntityNotFoundException("слово не существует по uid: " + uid);
        }

        Category category = categoryRepository.findByUid(dto.getCategoryUid())
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found with uid: " + dto.getCategoryUid()));

        return currentWord
                .map(word -> {
                    word.setName(dto.getName());
                    word.setClue(dto.getClue());
                    word.setTranslation(dto.getTranslation());
                    word.setCategory(category);
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

    public Page<Word> findAll(int page, int size, WordSortField wordSortField, Sort.Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, wordSortField.getFieldName());

        return wordRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Transactional(readOnly = true)
    public Optional<Word> findNextWord(@NonNull String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        List<Long> learnedWordIdList = userProgressService.findWordIdsByUser(user);

        Optional<Word> wordOp = wordRepository.findNextByUserCategory(user.getCurrentCategory(), learnedWordIdList);

        wordOp.ifPresent(word -> userProgressService.registerUserProgress(user, word));

        return wordOp;
    }

}