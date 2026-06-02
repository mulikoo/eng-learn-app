package com.mulikoo.englearnapp.mapper;

import com.mulikoo.englearnapp.dto.PhraseDto;
import com.mulikoo.englearnapp.entity.Phrase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface PhraseMapper {
    @Mapping(target = "categoryUid", source = "category.uid")
    PhraseDto toDto(Phrase phrase);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryUid", qualifiedByName = "CategoryUidToCategory")
    Phrase toEntity(PhraseDto phraseDto);
}
