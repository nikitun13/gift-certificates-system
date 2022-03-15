package com.epam.esm.mapper;

import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.UserDetailsDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "orders", ignore = true)
    User toUser(CreateUserDto createUserDto);

    UserDetailsDto toUserDetailsDto(User user);
}
