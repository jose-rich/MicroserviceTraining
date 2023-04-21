package com.microservice.authservice.mapper;

import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.model.Role;
import com.microservice.authservice.model.User;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-20T18:26:21+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( userEntity.getId() );
        userDto.setUsername( userEntity.getUsername() );
        userDto.setEmail( userEntity.getEmail() );
        userDto.setPassword( userEntity.getPassword() );
        Set<Role> set = userEntity.getRoles();
        if ( set != null ) {
            userDto.setRoles( new LinkedHashSet<Role>( set ) );
        }

        return userDto;
    }

    @Override
    public User toUserEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setUsername( userDto.getUsername() );
        user.setEmail( userDto.getEmail() );
        user.setPassword( userDto.getPassword() );
        Set<Role> set = userDto.getRoles();
        if ( set != null ) {
            user.setRoles( new LinkedHashSet<Role>( set ) );
        }

        return user;
    }

    @Override
    public List<UserDto> toUserDtoList(List<User> userEntityList) {
        if ( userEntityList == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( userEntityList.size() );
        for ( User user : userEntityList ) {
            list.add( toUserDto( user ) );
        }

        return list;
    }

    @Override
    public List<User> toUserEntityList(List<UserDto> userDtoList) {
        if ( userDtoList == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( userDtoList.size() );
        for ( UserDto userDto : userDtoList ) {
            list.add( toUserEntity( userDto ) );
        }

        return list;
    }
}
