package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.entity.UserProgress;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.UserProgressRepository;
import com.mulikoo.englearnapp.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;

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

    public List<Long> findWordIdsByUser(User user) {

        return userProgressRepository.findWordIdsByUser(user);
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
}