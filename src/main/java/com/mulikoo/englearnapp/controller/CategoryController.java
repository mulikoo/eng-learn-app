package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.enums.CategorySortField;
import com.mulikoo.englearnapp.mapper.CategoryMapper;
import com.mulikoo.englearnapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('CATEGORY_READ')")
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

    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    @GetMapping
    @Operation(summary = "Получение списка категорий", description = "Возвращает список категорий")
    public ResponseEntity<Page<CategoryDto>> getAllCategory(@RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size,
                                                            @RequestParam(name = "sortField", defaultValue = "NAME") CategorySortField categorySortField,
                                                            @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        log.info("попытка получения списка категорий");

        Page<Category> categoryPage = categoryService.findAll(page, size, categorySortField, sortDirection);
        Page<CategoryDto> categoryDtoPage = categoryPage.map(categoryMapper::toDto);

        return ResponseEntity.ok(categoryDtoPage);
    }

    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
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

    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
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

    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление категории", description = "Позволяет удалять каегорию")
    public ResponseEntity<CategoryDto> deleteCategory(@Parameter(description = "uid категории") @NotNull @PathVariable("uid") UUID uid) {
        log.info("удаление категории по uid: {}", uid.toString());

        categoryService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }

}