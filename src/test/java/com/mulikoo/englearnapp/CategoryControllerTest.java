package com.mulikoo.englearnapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulikoo.englearnapp.controller.CategoryController;
import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.mapper.CategoryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

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

    @DisplayName("получение категории по uid")
    @Test
    public void getCategoriesCase1() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{uid}", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("новичок"))
                .andExpect(jsonPath("$.description").value("базовый уровень"));


    }

    @DisplayName("создание новой категории")
    @Test
    public void postCategoriesCase1() throws Exception {
        var requestDto = new CategoryDto();
        requestDto.setName("новичок");
        requestDto.setDescription("базовый уровень");

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
