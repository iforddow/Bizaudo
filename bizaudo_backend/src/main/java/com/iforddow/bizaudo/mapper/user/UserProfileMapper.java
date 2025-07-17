package com.iforddow.bizaudo.mapper.user;

import com.iforddow.bizaudo.dto.user.UserProfileDTO;
import com.iforddow.bizaudo.jpa.entity.user.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserProfileDTO toProfileDTO(UserProfile userProfile);

}
