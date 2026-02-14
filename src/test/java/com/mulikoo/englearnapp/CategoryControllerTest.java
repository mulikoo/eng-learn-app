package com.mulikoo.englearnapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulikoo.englearnapp.controller.CategoryController;
import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.mapper.CategoryMapper;
import com.mulikoo.englearnapp.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    private final static String URL = "/api/v1/categories";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryMapper categoryMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @DisplayName("получение категории по uid")
    void getCategoriesCase1() throws Exception {
        UUID testUid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Category mockCategory = new Category();
        mockCategory.setUid(testUid);
        mockCategory.setName("новичок");
        mockCategory.setDescription("базовый уровень");

        CategoryDto mockDto = new CategoryDto();
        mockDto.setName("новичок");
        mockDto.setDescription("базовый уровень");

        when(categoryService.findByUid(testUid)).thenReturn(Optional.of(mockCategory));
        when(categoryMapper.toDto(mockCategory)).thenReturn(mockDto);

        mockMvc.perform(get(URL + "/{uid}", testUid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("новичок"))
                .andExpect(jsonPath("$.description").value("базовый уровень"));
    }

    @DisplayName("создание новой категории")
    @Test
    public void postCategoriesCase1() throws Exception {
        var requestDto = new CategoryDto();
        requestDto.setName("новичок");
        requestDto.setDescription("базовый уровень");

        Category categoryMokAnswer = new Category();

        CategoryDto categoryDtoAnswer = new CategoryDto();
        categoryDtoAnswer.setName("новичок");
        categoryDtoAnswer.setDescription("базовый уровень");
        categoryDtoAnswer.setUid(UUID.randomUUID());
        categoryDtoAnswer.setCreationDate(LocalDateTime.now());
        categoryDtoAnswer.setModificationDate(LocalDateTime.now());

        when(categoryService.create(requestDto)).thenReturn(Optional.of(categoryMokAnswer));
        when(categoryMapper.toDto(categoryMokAnswer)).thenReturn(categoryDtoAnswer);

        mockMvc.perform(
                        post(URL)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("новичок"))
                .andExpect(jsonPath("$.description").value("базовый уровень"))
                .andExpect(jsonPath("$.modificationDate").exists())
                .andExpect(jsonPath("$.creationDate").exists());

    }

    @DisplayName("обновляем категорию")
    @Test
    public void putCategoriesCase1() throws Exception {
        var requestDto = new CategoryDto();
        requestDto.setName("новичок");
        requestDto.setDescription("базовый уровень");

        CategoryService categoryMokAnswer = new CategoryService();
        CategoryDto categoryDtoAnswer = new CategoryDto();

        categoryDtoAnswer.setName("продвинутый");
        categoryDtoAnswer.setDescription("продвинут");
        categoryDtoAnswer.setUid(UUID.randomUUID());
        categoryDtoAnswer.setCreationDate(LocalDateTime.now());
        categoryDtoAnswer.setModificationDate(LocalDateTime.now());

        mockMvc.perform(
                        put("/api/v1/categories/{uid}", UUID.randomUUID().toString())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)

                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("новичок"))
                .andExpect(jsonPath("$.description").value("базовый уровень"))
                .andExpect(jsonPath("$.uid").exists())
                .andExpect(jsonPath("$.modificationDate").exists())
                .andExpect(jsonPath("$.creationDate").exists());
    }

    @DisplayName("удаление категории")
    @Test
    public void deleteCategoriesCase1() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{uid}", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}