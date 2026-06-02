package com.mulikoo.englearnapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulikoo.englearnapp.controller.CategoryController;
import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.enums.CategorySortField;
import com.mulikoo.englearnapp.mapper.CategoryMapper;
import com.mulikoo.englearnapp.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final UUID TEST_UID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final String TEST_NAME = "Fruits";
    private static final String TEST_DESCRIPTION = "Fresh fruits category";
    private static final String UPDATED_NAME = "Vegetables";
    private static final String UPDATED_DESCRIPTION = "Fresh vegetables category";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setUid(TEST_UID);
        category.setName(TEST_NAME);
        category.setDescription(TEST_DESCRIPTION);
        return category;
    }

    private CategoryDto createResponseDto() {
        CategoryDto dto = new CategoryDto();
        dto.setUid(TEST_UID);
        dto.setName(TEST_NAME);
        dto.setDescription(TEST_DESCRIPTION);
        return dto;
    }

    private CategoryDto createUpdateResponseDto() {
        CategoryDto dto = new CategoryDto();
        dto.setUid(TEST_UID);
        dto.setName(UPDATED_NAME);
        dto.setDescription(UPDATED_DESCRIPTION);
        return dto;
    }

    private CategoryDto createRequestDto() {
        CategoryDto dto = new CategoryDto();
        dto.setName(TEST_NAME);
        dto.setDescription(TEST_DESCRIPTION);
        return dto;
    }

    private CategoryDto createUpdateRequestDto() {
        CategoryDto dto = new CategoryDto();
        dto.setName(UPDATED_NAME);
        dto.setDescription(UPDATED_DESCRIPTION);
        return dto;
    }

    @Test
    void getCategory_shouldReturnCategoryDto_whenCategoryExists() throws Exception {
        Category category = createTestCategory();
        CategoryDto dto = createResponseDto();

        when(categoryService.findByUid(TEST_UID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/categories/{uid}", TEST_UID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(TEST_UID.toString()))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION));
    }

    @Test
    void getCategory_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        when(categoryService.findByUid(TEST_UID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/categories/{uid}", TEST_UID))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() throws Exception {
        Category savedCategory = createTestCategory();
        CategoryDto responseDto = createResponseDto();

        when(categoryService.create(any(CategoryDto.class))).thenReturn(Optional.of(savedCategory));
        when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(createRequestDto());

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uid").value(TEST_UID.toString()))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION));
    }

    @Test
    void createCategory_shouldReturnBadRequest_whenInvalidData() throws Exception {
        String invalidJson = "{\"name\":\"\",\"description\":\"\"}";

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        Category updatedCategory = createTestCategory();
        updatedCategory.setName(UPDATED_NAME);
        updatedCategory.setDescription(UPDATED_DESCRIPTION);
        CategoryDto responseDto = createUpdateResponseDto();

        when(categoryService.update(eq(TEST_UID), any(CategoryDto.class))).thenReturn(Optional.of(updatedCategory));
        when(categoryMapper.toDto(updatedCategory)).thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(createUpdateRequestDto());

        mockMvc.perform(put("/api/v1/categories/{uid}", TEST_UID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.description").value(UPDATED_DESCRIPTION));
    }

    @Test
    void updateCategory_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        when(categoryService.update(eq(TEST_UID), any(CategoryDto.class))).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(createUpdateRequestDto());

        mockMvc.perform(put("/api/v1/categories/{uid}", TEST_UID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{uid}", TEST_UID))
                .andExpect(status().isNoContent());
    }
}