package com.iforddow.bizaudo.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.iforddow.bizaudo.jpa.entity.rbac.Role;
import com.iforddow.bizaudo.jpa.entity.user.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id, String email, boolean enabled, boolean emailVerified,
                      Instant lastActive, @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
                      Set<String> roles, @JsonInclude(JsonInclude.Include.NON_NULL) UserProfileDTO userProfile) {

    public UserDTO(User user, boolean withProfile) {
        this(
                user.getId(),
                user.getEmail(),
                user.getEnabled(),
                user.getEmailVerified(),
                user.getLastActive(),
                extractRoles(user),
                withProfile ? user.getProfile() != null ? new UserProfileDTO(user.getProfile(), false) : null : null
        );
    }

    private static Set<String> extractRoles(User user) {
        Set<String> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getCodeName());
        }
        return roles;
    }

}
