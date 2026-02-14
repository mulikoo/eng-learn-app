package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.mapper.CategoryMapper;
import com.mulikoo.englearnapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/categories")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @GetMapping("/{uid}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("uid") UUID uid) {
        log.info("попытка получения категория по uid: {}", uid.toString());

        Optional<Category> result = categoryService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoryMapper.toDto(result.get()));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        log.info("создание новой категории. получили name{}", categoryDto.getName());

        Optional<Category> result = categoryService.create(categoryDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(categoryMapper.toDto(result.get()), HttpStatus.CREATED);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("uid") UUID uid, @RequestBody CategoryDto categoryDto) {
        log.info("обновление категории по uid: {}", uid.toString());

        Optional<Category> result = categoryService.update(uid, categoryDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoryMapper.toDto(result.get()));
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable("uid") UUID uid) {
        log.info("удаление категории по uid: {}", uid.toString());

        categoryService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }

}