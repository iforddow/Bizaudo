package com.iforddow.bizaudo.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iforddow.bizaudo.jpa.entity.rbac.Role;
import com.iforddow.bizaudo.jpa.entity.user.User;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDTO(UUID id, String email, boolean enabled, boolean emailVerified,
                      Instant lastActive, Set<String> roles,
                      @JsonInclude(JsonInclude.Include.NON_NULL) UserProfileDTO userProfile) {

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
        return user.getRoles().stream()
                .map(Role::getCodeName)
                .collect(Collectors.toSet());
    }

}
