package com.mulikoo.englearnapp.mapper;

import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class})
public interface UserMapper {

    @Mapping(target = "currentCategoryUid", source = "currentCategoryId", qualifiedByName = "CategoryIdToUid")
    UserDto toDto(User user);

    @Mapping(target = "currentCategoryId", source = "currentCategoryUid", qualifiedByName = "CategoryUidToId")
    User toEntity(UserDto userDto);

}
