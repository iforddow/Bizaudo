package com.iforddow.bizaudo.mapper;

import com.iforddow.bizaudo.dto.user.UserPublicDTO;
import com.iforddow.bizaudo.jpa.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserPublicDTO toPublicDTO(User user);

}
