package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.view.UserProgressView;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.entity.UserProgress;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import com.mulikoo.englearnapp.exceptions.AttemptCounterException;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.UserProgressRepository;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;

    private static final int MAX_ATTEMPT_COUNTER = 3;

    public List<UserProgress> findAll() {
        return userProgressRepository.findAll();
    }

    public UserProgress save(UserProgress userProgress) {
        return userProgressRepository.save(userProgress);
    }

    public void deleteById(@Nullable Long id) {
        if (id == null) {
            log.warn("id is null");
            return;
        }
        userProgressRepository.deleteById(id);
    }

    public List<UserProgressView> findWordIdsByUser(@NonNull User user, @NonNull Category category) {

        return userProgressRepository.findWordIdsByUser(user, category);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserProgress registerUserProgress(@NonNull User user, @NonNull Word word) {
        log.info("регистрация прогресса по слову с UUID: {} для пользователя: {}", user.getUid(), user.getUsername());

        UserProgress userProgress = new UserProgress();
        userProgress.setStatus(UserProgressStatus.SENT);
        userProgress.setUser(user);
        userProgress.setUid(UUID.randomUUID());
        userProgress.setWord(word);

        return userProgressRepository.save(userProgress);
    }

    public void updateProgress(@NonNull UUID wordUid, @NonNull String username, boolean isCorrectAnswer) {
        UserProgress userProgress = userProgressRepository.findByWordUidAndUsername(wordUid, username)
                .orElseThrow(() -> new EntityNotFoundException("Для пользователя %s не найден прогресс по слову с uid: %s"
                        .formatted(username, wordUid)));

        int count = userProgress.getAttemptCounter();

        if (count >= MAX_ATTEMPT_COUNTER) {
            userProgress.setStatus(UserProgressStatus.FAILED);
            userProgressRepository.save(userProgress);
            throw new AttemptCounterException("Превышено число попыток для слова с uid: %s для пользователя: %s"
                    .formatted(wordUid, username));
        }

        userProgress.setAttemptCounter(++count);

        if (isCorrectAnswer) {
            userProgress.setStatus(UserProgressStatus.LEARNED);
            log.info("Правильный ответ, статус обновлён для прогресса с uid: {}", userProgress.getUid());
        } else {
            userProgress.setStatus(UserProgressStatus.IN_PROGRESS);
            log.info("Неправильный ответ для прогресса с uid: {}, попытка {}/3", userProgress.getUid(), userProgress.getAttemptCounter());
        }

        userProgressRepository.save(userProgress);
    }

    /**
     * Получение UserProgress по uid слова и username пользователя
     *
     * @param wordUid  uid слова
     * @param username юзернейм пользователя
     * @return UserProgress
     */
    public Optional<UserProgress> getUserProgressByWordUid(@NonNull UUID wordUid, @NonNull String username) {

        return userProgressRepository.findByWordUidAndUsername(wordUid, username);
    }

}