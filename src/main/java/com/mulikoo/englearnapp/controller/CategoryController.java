package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RequestMapping("/api/v1/categories")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryMapper categoryMapper;

    @GetMapping("/{uid}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("uid") UUID uid) {
        log.info("попытка получения категория по uid: {}", uid.toString());

        var mockResponse = new CategoryDto();
        mockResponse.setUid(uid);
        mockResponse.setName("name");
        mockResponse.setDescription("description");
        mockResponse.setCreationDate(LocalDateTime.now());
        mockResponse.setModificationDate(LocalDateTime.now());

        return ResponseEntity.ok(mockResponse);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        log.info("создание новой категории. получили name{}", categoryDto.getName());

        var mockResponse = new CategoryDto();
        mockResponse.setName(categoryDto.getName());
        mockResponse.setDescription(categoryDto.getDescription());
        mockResponse.setCreationDate(LocalDateTime.now());
        mockResponse.setModificationDate(LocalDateTime.now());

        return new ResponseEntity<>(mockResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID uid,@RequestBody CategoryDto categoryDto) {
        log.info("обновление категории по uid: {}", uid.toString());

        var mockResponse = new CategoryDto();
        mockResponse.setUid(uid);
        mockResponse.setCreationDate(LocalDateTime.now());
        mockResponse.setModificationDate(LocalDateTime.now());

        return ResponseEntity.ok(mockResponse);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable UUID uid) {
        log.info("удаление категории по uid: {}", uid.toString());

        return ResponseEntity.ok().build();
    }

}