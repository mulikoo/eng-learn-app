package com.mulikoo.englearnapp;

import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.exceptions.EntityAlreadyExistsException;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findByUid_ShouldReturnCategory_WhenUidIsValid() {
        UUID uid  = UUID.randomUUID();
        Category expectedCategory = new Category();
        expectedCategory.setUid(uid);
        expectedCategory.setName("IT Vocabulary");

        when(categoryRepository.findByUid(uid)).thenReturn(Optional.of(expectedCategory));

        Optional<Category> actualCategory = categoryService.findByUid(uid);

        assertTrue(actualCategory.isPresent());
        assertEquals(uid, actualCategory.get().getUid());
        assertEquals("IT Vocabulary", actualCategory.get().getName());
        verify(categoryRepository, times(1)).findByUid(uid);
    }

    @Test
    void create_ShouldThrowEntityAlreadyExistsException_WhenCategoryNameExists() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Travel");

        when(categoryRepository.existsByName("Travel")).thenReturn(true);

        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> {
            categoryService.create(dto);
        });

        assertEquals("category already exists", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void create_ShouldSaveCategory_WhenNameIsUnique() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Phrasal Verbs");
        dto.setDescription("Useful verbs");

        Category savedCategory = new Category();
        savedCategory.setName(dto.getName());
        savedCategory.setDescription(dto.getDescription());

        when(categoryRepository.existsByName("Phrasal Verbs")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Optional<Category> result = categoryService.create(dto);

        assertTrue(result.isPresent());
        assertEquals("Phrasal Verbs", result.get().getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void update_ShouldUpdateAndSaveCategory_WhenCategoryExists() {
        UUID uid = UUID.randomUUID();
        CategoryDto dto = new CategoryDto();
        dto.setName("Updated Name");
        dto.setDescription("Updated Desc");

        Category existingCategory = new Category();
        existingCategory.setUid(uid);
        existingCategory.setName("Old Name");

        when(categoryRepository.findByUid(uid)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Category> result = categoryService.update(uid, dto);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals("Updated Desc", result.get().getDescription());
        verify(categoryRepository, times(1)).save(existingCategory);
    }
}