package com.debtcollection.mapper;

import com.debtcollection.dto.user.UserCreateDto;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    User toEntity(UserCreateDto dto);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "endClient.id", target = "endClientId")
    UserDto toDto(User user);

}

