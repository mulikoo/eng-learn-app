package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.mapper.CategoryMapper;
import com.mulikoo.englearnapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/categories")
@Tag(name = "Контроллер для категорий", description = "Контроллер для управления категориями")
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @GetMapping("/{uid}")
    @Operation(summary = "Получение категории по uid", description = "Возвращает категорию")
    public ResponseEntity<CategoryDto> getCategory(@Parameter(description = "uid категории") @NotNull @PathVariable("uid") UUID uid) {
        log.info("попытка получения категория по uid: {}", uid.toString());

        Optional<Category> result = categoryService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoryMapper.toDto(result.get()));
    }

    @PostMapping
    @Operation(summary = "Создание категории", description = "Позволяет создавать каегорию")
    public ResponseEntity<CategoryDto> createCategory(@Validated @RequestBody CategoryDto categoryDto) {
        log.info("создание новой категории. получили name{}", categoryDto.getName());

        Optional<Category> result = categoryService.create(categoryDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(categoryMapper.toDto(result.get()), HttpStatus.CREATED);
    }

    @PutMapping("/{uid}")
    @Operation(summary = "Обновление категории", description = "Позволяет обновлять каегорию")
    public ResponseEntity<CategoryDto> updateCategory(@Parameter(description = "uid категории")
                                                      @NotNull @PathVariable("uid") UUID uid, @Validated @RequestBody CategoryDto categoryDto) {
        log.info("обновление категории по uid: {}", uid.toString());

        Optional<Category> result = categoryService.update(uid, categoryDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoryMapper.toDto(result.get()));
    }

    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление категории", description = "Позволяет удалять каегорию")
    public ResponseEntity<CategoryDto> deleteCategory(@Parameter(description = "uid категории") @NotNull @PathVariable("uid") UUID uid) {
        log.info("удаление категории по uid: {}", uid.toString());

        categoryService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }

}