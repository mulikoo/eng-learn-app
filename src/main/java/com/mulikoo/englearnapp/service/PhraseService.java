package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.PhraseDto;
import com.mulikoo.englearnapp.dto.view.UserProgressView;
import com.mulikoo.englearnapp.dto.view.UserProgressViewPhrase;
import com.mulikoo.englearnapp.entity.*;
import com.mulikoo.englearnapp.enums.ClueType;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import com.mulikoo.englearnapp.exceptions.*;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.PhraseRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhraseService {

    private final PhraseRepository phraseRepository;
    private final CategoryRepository categoryRepository;
    private final UserProgressService userProgressService;
    private final UserService userService;

    public Optional<Phrase> findByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return Optional.empty();
        }

        return phraseRepository.findByUid(uid);
    }

    @Transactional
    public Optional<Phrase> create(@NonNull PhraseDto dto) {

        if (phraseRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())) {
            throw new EntityAlreadyExistsException("фраза уже существует");
        }

        Category category = categoryRepository.findByUid(dto.getCategoryUid())
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found"));

        Phrase phrase = new Phrase();

        phrase.setUid(UUID.randomUUID());
        phrase.setName(dto.getName());
        phrase.setClue(dto.getClue());
        phrase.setTranslation(dto.getTranslation());
        phrase.setCategory(category);

        return Optional.of(phraseRepository.save(phrase));
    }

    @Transactional
    public Optional<Phrase> update(@NonNull UUID uid, @NonNull PhraseDto dto) {

        Optional<Phrase> currentPhrase = findByUid(uid);
        if (currentPhrase.isEmpty()) {
            throw new EntityNotFoundException("фразы не существует по uid: " + uid);
        }

        Category category = categoryRepository.findByUid(dto.getCategoryUid())
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found with uid: " + dto.getCategoryUid()));

        return currentPhrase
                .map(phrase -> {
                    phrase.setName(dto.getName());
                    phrase.setClue(dto.getClue());
                    phrase.setTranslation(dto.getTranslation());
                    phrase.setCategory(category);
                    return phraseRepository.save(phrase);
                });
    }

    @Transactional
    public void deleteByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return;
        }
        phraseRepository.deleteByUid(uid);
    }

    @Transactional(readOnly = true)
    public Optional<Phrase> findNextPhrase(@NonNull String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Category currentCategory = user.getCurrentCategory();

        List<UserProgressViewPhrase> learnedList = userProgressService.findPhraseIdByUser(user, currentCategory);

        boolean isHasInLearning = learnedList.stream()
                .anyMatch(view -> UserProgressStatus.IN_PROGRESS.equals(view.getStatus())
                        || UserProgressStatus.SENT.equals(view.getStatus()));
        if (isHasInLearning) {
            throw new HasWordAlreadyInLearningException("У пользователя %s уже есть фразы на изучении".formatted(username));
        }

        List<Long> phraseIds = learnedList.stream()
                .map(view -> view.getPhraseId())
                .distinct()
                .collect(Collectors.toList());

        Optional<Phrase> phraseOp = phraseRepository.findNextByUserCategory(user.getCurrentCategory(), phraseIds);

        if (phraseOp.isEmpty()) {
            throw new AvailableWordsNotFoundInCategoryException("фразы по данной категории: %s закончились"
                    .formatted(user.getCurrentCategory().getName()));
        }

        phraseOp.ifPresent(phrase -> userProgressService.registerUserProgressForPhrase(user, phrase));

        return phraseOp;
    }

    /**
     * проверяет корректность перевода
     *
     * @param uid         uid фразы
     * @param translation предполагаемый перевод фразы
     * @param username    юзернейм пользователя
     * @return true - правильный перевод, false - не правильный перевод
     */
    @Transactional(noRollbackFor = AttemptCounterException.class)
    public boolean isTranslationCorrect(@NonNull UUID uid, @NonNull String translation, @NonNull String username) {
        String actualTranslation = phraseRepository.findTranslationByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("Phrase not found" + uid));

        boolean isCorrect = actualTranslation.equalsIgnoreCase(translation);
        userProgressService.updateProgressPhrase(uid, username, isCorrect);
        return isCorrect;
    }

    @Transactional
    public String getClue(@NonNull UUID phraseUid, @NonNull String username) {

        UserProgress userProgress = userProgressService.getUserProgressByPhraseUid(phraseUid, username)
                .orElseThrow(() -> new EntityNotFoundException(("Для пользователя '%s' и фразы '%s' ещё не начато изучение"
                        .formatted(username, phraseUid)
                )));

        String clueFromWord = phraseRepository.findClueByUid(phraseUid)
                .orElseThrow(() -> new EntityNotFoundException(("подсказка для фразы с uid '%s' не найдена".formatted(phraseUid))));

        if (userProgress.getUserClueTypes() != null && !userProgress.getUserClueTypes().contains(ClueType.TEXT)) {
            userProgress.getUserClueTypes().add(ClueType.TEXT);
        } else {
            userProgress.setUserClueTypes(new HashSet<>(Set.of(ClueType.TEXT)));
        }

        return clueFromWord;
    }

}
