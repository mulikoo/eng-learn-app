package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.UserRepository;
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
public class UserService {
    private final static String DEFAULT_CATEGORY_NAME = "Базовый английский";

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Optional<User> findByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.info("uid is null");
            return Optional.empty();
        }
        return userRepository.findByUid(uid);
    }

    @Transactional
    public Optional<User> create(@NonNull UserDto dto) {
        Optional<Long> categoryIdOp = categoryRepository.findIdByName(DEFAULT_CATEGORY_NAME);

        if (categoryIdOp.isEmpty()) {
            throw new EntityNotFoundException("Не найдена дефолтная категория");
        }

        User user = new User();

        user.setUid(UUID.randomUUID());
        user.setUsername(dto.getUsername());
        user.setCurrentCategoryId(categoryIdOp.get());
        user.setCreationDate(LocalDateTime.now());
        user.setModificationDate(LocalDateTime.now());

        return Optional.of(userRepository.save(user));

    }

    @Transactional
    public Optional<User> update(@NonNull UUID uid, @NonNull UserDto userDto) {
        Optional<User> currentUser = findByUid(uid);
        if (currentUser.isEmpty()) {
            log.warn("uid is null");

            throw new EntityNotFoundException("uid not found" + uid);
        }

        Optional<Long> categoryIdOp = categoryRepository.findIdByUid(userDto.getCurrentCategoryUid());

        if (categoryIdOp.isEmpty()) {
            throw new EntityNotFoundException("Category Not Found with uid: " + userDto.getCurrentCategoryUid());
        }

        return currentUser
                .map(user -> {
                    user.setUsername(userDto.getUsername());
                    user.setCurrentCategoryId(categoryIdOp.get());
                    user.setModificationDate(LocalDateTime.now());
                    return userRepository.save(user);
                });

    }

    @Transactional
    public void deleteByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return;
        }

        userRepository.deleteByUid(uid);
    }
}