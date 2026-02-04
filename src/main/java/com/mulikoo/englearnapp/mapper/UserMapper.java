package com.mulikoo.englearnapp.mapper;

import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "currentCategoryUid", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "currentCategoryId", ignore = true)
    User toEntity(UserDto userDto);

}
