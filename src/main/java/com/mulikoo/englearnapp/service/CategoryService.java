package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.exceptions.EntityAlreadyExistsException;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> findByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return Optional.empty();
        }
        return categoryRepository.findByUid(uid);
    }

    @Transactional
    public Optional <Category> create(@NonNull CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            log.warn("category already exists");

            throw new EntityAlreadyExistsException("category already exists");
        }

        Category category = new Category();

        category.setUid(UUID.randomUUID());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setCreationDate(LocalDateTime.now());
        category.setModificationDate(LocalDateTime.now());

        return Optional.of(categoryRepository.save(category));
    }

    @Transactional
    public Optional<Category> update(@NonNull UUID uid, @NonNull CategoryDto categoryDto) {
        Optional<Category> currentCategory = findByUid(uid);
        if (currentCategory.isEmpty()) {
            log.warn("category с uid: {} не существует", uid);
            throw new EntityNotFoundException("category не существует");
        }

        return currentCategory
                .map(category -> {
                    category.setName(categoryDto.getName());
                    category.setDescription(categoryDto.getDescription());
                    category.setModificationDate(LocalDateTime.now());
                    return categoryRepository.save(category);
                });
    }

    @Transactional
    public void deleteByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return;
        }
        categoryRepository.deleteByUid(uid);
    }

}
