package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.Role;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.enums.UserSortField;
import com.mulikoo.englearnapp.exceptions.EntityAlreadyExistsException;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.RoleRepository;
import com.mulikoo.englearnapp.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final static String DEFAULT_CATEGORY_NAME = "Базовый английский";
    private final static String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public Optional<User> findByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.info("uid is null");
            return Optional.empty();
        }
        return userRepository.findByUid(uid);
    }

    public Optional<User> findByUsername(@Nullable String username) {
        if (username == null) {
            log.info("username is null");
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }

    @Transactional
    public Optional<User> create(@NonNull String username, @NonNull String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new EntityAlreadyExistsException("пользователь с таким username '%s' уже существует".formatted(username));
        }

        Category category = categoryRepository.findByName(DEFAULT_CATEGORY_NAME)
                .orElseThrow(() -> new EntityNotFoundException("Не найдена дефолтная категория"));

        Role role = roleRepository.findByCode(DEFAULT_ROLE)
                .orElseThrow(() -> new EntityNotFoundException("не найдена дефолтная роль " + DEFAULT_ROLE));

        User user = new User();

        user.setUid(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCurrentCategory(category);
        user.setRole(role);

        return Optional.of(userRepository.save(user));
    }

    @Transactional
    public Optional<User> update(@NonNull UUID uid, @NonNull UserDto userDto) {
        Optional<User> currentUser = findByUid(uid);
        if (currentUser.isEmpty()) {
            throw new EntityNotFoundException("user not found by uid: " + uid);
        }

        Long categoryId = categoryRepository.findIdByUid(userDto.getCurrentCategoryUid())
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found with uid: " + userDto.getCurrentCategoryUid()));

        Category category = categoryRepository.getReferenceById(categoryId);

        return currentUser
                .map(user -> {
                    user.setUsername(userDto.getUsername());
                    user.setCurrentCategory(category);
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

    public Page<User> findAll(int page, int size, UserSortField userSortField, Sort.Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, userSortField.getFieldName());

        return userRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Transactional
    public void changeCategory(UUID uidCategory, String username){
        Category category = categoryRepository.findByUid(uidCategory)
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found with uid: " + uidCategory));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Username Not Found with uid: " + username));
        user.setCurrentCategory(category);
    }
}