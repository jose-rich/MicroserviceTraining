package com.microservice.authservice.mapper;

import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User userEntity);

    User toUserEntity (UserDto userDto);

    List<UserDto> toUserDtoList(List<User> userEntityList);

    List<User> toUserEntityList (List<UserDto> userDtoList);


}
