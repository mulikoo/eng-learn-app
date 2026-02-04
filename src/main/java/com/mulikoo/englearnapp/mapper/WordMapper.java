package com.mulikoo.englearnapp.mapper;

import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.dto.WordDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WordMapper {

    @Mapping(target = "categoryUid", ignore = true)
    WordDto toDto(Word word);

    @Mapping(target = "categoryId", ignore = true)
    Word toEntity(WordDto wordDto);
}
