package com.iforddow.bizaudo.dto.user;

import com.iforddow.bizaudo.jpa.entity.Role;
import com.iforddow.bizaudo.jpa.entity.user.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id, String email, boolean enabled, boolean emailVerified,
                      Instant lastActive, Set<String> roles, UserProfileDTO userProfile) {

    public UserDTO(User user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getEnabled(),
                user.getEmailVerified(),
                user.getLastActive(),
                extractRoles(user),
                user.getProfile() != null ? new UserProfileDTO(user.getProfile(), false) : null
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
