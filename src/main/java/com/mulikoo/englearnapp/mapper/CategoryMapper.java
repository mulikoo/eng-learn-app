package com.mulikoo.englearnapp.mapper;

import com.mulikoo.englearnapp.dto.CategoryDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapper {

    @Autowired
    protected CategoryRepository categoryRepository;

    public abstract CategoryDto toDto(Category category);

    public abstract Category toEntity(CategoryDto categoryDto);

    @Named("CategoryUidToCategory")
    protected Category categoryUidToId(UUID categoryUid) {
        if (categoryUid == null) {
            return null;
        }

        return categoryRepository.findByUid(categoryUid)
                .orElse(null);
    }
}
