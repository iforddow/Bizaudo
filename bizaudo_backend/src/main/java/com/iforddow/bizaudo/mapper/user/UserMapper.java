package com.iforddow.bizaudo.mapper.user;

import com.iforddow.bizaudo.dto.user.UserDTO;
import com.iforddow.bizaudo.dto.user.UserWithProfileDTO;
import com.iforddow.bizaudo.jpa.entity.Role;
import com.iforddow.bizaudo.jpa.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", source = "roles")
    UserDTO toUserPublicDTO(User user);

    @Mapping(target = "roles", source = "roles")
    UserWithProfileDTO toUserWithProfileDTO(User user);

    default Set<String> map(Set<Role> roles) {
        return roles.stream()
                .map(Role::getCodeName)
                .collect(Collectors.toSet());
    }

}
