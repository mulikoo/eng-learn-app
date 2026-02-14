package com.mulikoo.englearnapp.mapper;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.entity.Word;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class})
public interface WordMapper {

    @Mapping(target = "categoryUid", source = "categoryId", qualifiedByName = "CategoryIdToUid")
    WordDto toDto(Word word);

    @Mapping(target = "categoryId", source = "categoryUid", qualifiedByName = "CategoryUidToId")
    Word toEntity(WordDto wordDto);
}
